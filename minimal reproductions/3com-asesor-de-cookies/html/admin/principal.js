function guardar() {
	var datos = {
		action: 'guardar_config',
		texto_aviso: jQuery( '#texto_aviso' ).val(),
		save_nonce: cdp_cookies_info.save_nonce,
	};
	jQuery.post( ajaxurl, datos, function( resul ) { alert(resul.ok ? "ok" : "bad"); }, 'json' );
}
jQuery( document ).ready( function( $ ) {
	$( 'a.cdp-cookies-guardar' ).click( function() { guardar(); } );
} );