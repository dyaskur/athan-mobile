/** Features */
$(document).ready(function() {
	jq('ul.img_features li').onShow(function() {
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
