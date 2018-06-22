/*初始化坦克大战路径，不使用common.js*/
(function($){
	var thisScriptPath = $('script').filter(function(){
		return !!this.src.match('/static/plugins/tankwar/js/init.js$');
	}).last()[0].src;
	$.tank = {
			/*项目路径*/
			rootPath:thisScriptPath.replace('page/static/plugins/tankwar/js/init.js', '')
	}
})(jQuery);	

