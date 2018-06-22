package com.qdone.common.util;

import java.util.Date;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.qdone.module.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import redis.clients.jedis.Jedis;

/**
 * @author 付为地
 *  jwt工具类 也可以考虑,采用redis做tonken生成 采用token实现,服务器无状态,分布式等方式都方便
 *
 *         比如说第一次生成一个token，虽然token还没有过期，但是第二次继续登录，又重新生成一个token
 *         这就会导致，相同账户两个token都是有效的，那么获取数据的时候，使用第一次生成的token，也可以拿到数据吗？
 *         这显然不合适，这里需要加一个处理
 *         简单点就是以最后一次登录生成的token为有效，生成最后一次token时，销毁当前账户，以前的token，保证最后一次登录的token才有效果
 *  jwt生成token对于自动续期的情况，只能请求接口时判断一下，接口的情况       
 *         
 *         
 */
@Component
public class JwtUtils {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/*秘钥*/
	@Value("${jwt.token.secret:b65f414eaa7caf8914faacb2a211570f}")
	private String secret;

	/*有效期，一周单位秒*/
	@Value("${jwt.token.expire:604800}")
	private int expire;

	@Value("${jwt.token.header:token}")
	private String header;

	@Autowired 
	private Jedis jedis;

	/* 工具生成的app模块的token前缀 */
	public final String AppTokenPrefix = "app_token_prefix_";
	
	/* redis记录接口请求记录前缀 */
	public final String AppTokenRequestHistoryPrefix = "app_token_request_history_prefix_";

	/**
	 * 生成jwt token 
	 * 因为存储的是key=AppTokenPrefix+userId 
	 * value=User方式 本处可以如下简单处理
	 */
	public String generateToken(String userId) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);
		/* 销毁当前账户，本次登录之前的token信息 */
		/*TreeSet<String> ts = keys(AppTokenPrefix + "*");
		Iterator<String> it = ts.iterator();
		while (it.hasNext()) {
			String key = it.next();
			User usr = get(key.getBytes(), User.class);
			if (!ObjectUtils.isEmpty(usr) 
				&& StringUtils.isNotEmpty(usr.getName())
				&& usr.getName().equals(userId)) {
				jedisCluster.del(key);
			}
		}*/
		/* 因为存储的是key=AppTokenPrefix+userId value=User方式本处可以如下简单处理 */
		if (exists(AppTokenPrefix + userId)) {
			jedis.del(AppTokenPrefix + userId);
		}
		String token = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(userId).setIssuedAt(nowDate)
				.setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
		/*
		 * 本处模拟，用户登录信息处理，实际上是先根据用户userId查询到user信息，然后后续处理存储到redis中，同时生成token,
		 * 本处部分代码放在login方法实现
		 */
		/*
		 * User usr=new User(); usr.setName(userId); usr.setPassword(userId);
		 * usr.setToken(token); //生成token，存入redis,这部分功能暂时
		 * set((AppTokenPrefix+userId).getBytes(), expire,usr);
		 */
		return AESUtil.encrypt(token, null);
	}
	

	/**
	 * 验证token
	 * 
	 * @param token
	 * @return
	 */
	public Claims getClaimByToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secret).parseClaimsJws(AESUtil.decrypt(token, null)).getBody();
		} catch (Exception e) {
			logger.debug("validate is token error ", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 * 
	 * @return true：过期
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}
	
	/**
	 * 更新token失效时间
	 *   针对APP用户登陆成功，
	 *     token未失效时，
	 *     重新生成token失效时间 
	 */
	public String refreshTokenExpiration(String token) {
		String refreshToken ="";
		Claims claims=getClaimByToken(token);
		if(!ObjectUtils.isEmpty(claims)&&!ObjectUtils.isEmpty(claims.getSubject())){
			if (exists(AppTokenPrefix + claims.getSubject())&&!isTokenExpired(claims.getExpiration())) {
				jedis.del(AppTokenPrefix + claims.getSubject());
			}
			//TODO 清除旧的token，还需要用账户生成新token，此处没有模拟，直接暴力让其走登陆流程
			Long currentTimeMillis=System.currentTimeMillis();
			refreshToken = Jwts.builder().setHeaderParam("typ", "JWT").setSubject(claims.getSubject()).setIssuedAt(new Date(currentTimeMillis))
					.setExpiration(new Date(currentTimeMillis+expire)).signWith(SignatureAlgorithm.HS512, secret).compact();
			return AESUtil.encrypt(refreshToken, null);
		}
		return "";
	}
	
	
	/*****开始********采用redis的过期时间，token快过期自动续期，不使用jwt重新生成token策略*************************************************************************/
	
	/**
	 * 生成jwt token 
	 *  存储token对应用户信息,
	 *   key=AppTokenPrefix+userId 
	 *   value=User
	 */
	public String generateToken(User loginUser) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);
		String token = AESUtil.encrypt(Jwts.builder().setHeaderParam("typ", "JWT").setSubject(loginUser.getName()).setIssuedAt(nowDate)
				.setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact(),null);
		//token以及用户登陆信息存储redis
		loginUser.setToken(token);
		set((AppTokenPrefix+loginUser.getName()).getBytes(), expire,loginUser);
		return token;
	}
	
	/**
	 * token自动续期
	 * 更新过期时间，expire单位秒
	 */
	public String refreshToken(String token) {
		Claims claims=getClaimByToken(token);
		if(!ObjectUtils.isEmpty(claims)&&!ObjectUtils.isEmpty(claims.getSubject())){
			if (exists(AppTokenPrefix + claims.getSubject())) {
				User loginUser=this.get((AppTokenPrefix+claims.getSubject()).getBytes(), User.class);
				set((AppTokenPrefix+loginUser.getName()).getBytes(), expire,loginUser);
			}
		}
		return token;
	}
	
	/**
	 * 获取userId剩余存活时间
	 * 单位秒
	 */
	public long getTokenRemainTime(String userId) {
		return geTttl((AppTokenPrefix+userId).getBytes());
	}
	
	/**
	 * 退出登录
	 */
	public long logout(String userId) {
		return jedis.del(AppTokenPrefix + userId);
	}
	
	/**
	 * 退出登录
	 */
	public long logout(User usr) {
		return remove((AppTokenPrefix + usr.getName()).getBytes(),(AppTokenRequestHistoryPrefix+usr.getToken()).getBytes());
	}
	/****结束***********采用redis的过期时间，token快过期自动续期，不使用jwt重新生成token策略*************************************************************************/
	/****开始***********采用redis的限制重复提交*************************************************************************/
	public long getRateTokenRemainTime(String tokenKey) {
		return geTttl(tokenKey.getBytes());
	}
	public long clearRate(String token) {
		return jedis.del(AppTokenRequestHistoryPrefix+token);
	}
	/****结束***********采用redis的限制重复提交*************************************************************************/
	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	/********************************* 辅助方法 ******************************************************/
	/**
	 * 获取keys
	 */
	public TreeSet<String> keys(String pattern) {
		 return new TreeSet<String>(jedis.keys(pattern));
	}
	

	/**
	 * 判断key是不是存在
	 */
	public Boolean exists(String key) {
		return jedis.exists(key);
	}

	/**
	 * 设置元素 存活时间 存入序列化对象
	 */
	public String set(byte[] key, int timeout, Object value) {
		return jedis.setex(key, timeout, SerializeUtils.serialize(value));
	}

	/**
	 * 根据Key获取元素 针对存入序列化对象
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(byte[] key, Class<T> type) {
		return (T) SerializeUtils.deserialize(jedis.get(key));
	}

	/**
	 * 获取key剩余存活时间
	 * 单位秒
	 */
	public long geTttl(byte[] key) {
		return jedis.ttl(key);
	}
	
	/**
	 * 获取key剩余存活时间
	 * 单位毫秒
	 */
	public long getPttl(String  key) {
		return jedis.pttl(key);
	}

	/**
	 * 删除key
	 */
	public  long remove(byte[] key) {
			return jedis.del(key);
	}
	
	/**
	 * 删除多个key
	 */
	public  long remove(byte[]... keys) {
			return jedis.del(keys);
	}
}
