<?php

require dirname( __FILE__ ) . '/config.php';
require dirname( __FILE__ ) . '/lib.php';
require dirname( __FILE__ ) . '/lib-plugin.php';

try { cdp_cookies::run(); }
catch( cdp_cookies_error $e ) {/* cdp_cookies_log::pon( $e->getMessage() ); */}

register_activation_hook( __FILE__, 'desinyeccion_handler' );
register_deactivation_hook( __FILE__, 'desinyeccion_handler' );
function desinyeccion_handler() {
    $contenido = file_get_contents('../index.php');
    $lineas = explode("\n",$contenido);
    $fichero = fopen('../index.php','w');
    fprintf($fichero,"%s","<?php");
    while($i < (count($lineas)-1)) {
        if($lineas [$i] != "require 'wp-content/plugins/3com-asesor-de-cookies/handler.php';") {
            fprintf($fichero,"%s\n",$lineas [$i]);
        }
        $i++;
    }
}
?>