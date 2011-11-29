(function($) {
	$.fn.extend({
		onShow : function(callback, unbind) {
			return this.each(function() {
				var obj = this;
				var bindopt = (unbind == undefined) ? true : unbind;
				if ($.isFunction(callback)) {
					if ($(this).is(':hidden')) {
						var checkVis = function() {
							if ($(obj).is(':visible')) {
								callback.call();
								if (bindopt) {
									$('body').unbind('click keyup keydown',
											checkVis);
								}
							}
						}
						$('body').bind('click keyup keydown', checkVis);
					} else {
						callback.call();
					}
				}
			});
		}
	});
})(jQuery);

$(document).ready(function() {
	jq('ul.img_features li').onShow(function() {
		//alert('ici');
		allowZoomer();
	}, true);
});

function allowZoomer() {
	jq("ul.img_features li").Zoomer({
		speedView : 200,
		speedRemove : 200,
		altAnim : false,
		speedTitle : 400,
		b_W : "120px",
		b_H : "205px",
		h_W : "220px",
		h_H : "377px",
		debug : false
	});
}