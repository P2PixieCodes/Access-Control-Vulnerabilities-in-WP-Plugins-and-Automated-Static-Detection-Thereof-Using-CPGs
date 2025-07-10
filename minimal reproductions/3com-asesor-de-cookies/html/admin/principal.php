<?php
    $mapa_web               =   cdp_cookies::parameter( 'enlace_mapa_web' );
    $aviso_legal            =   cdp_cookies::parameter( 'enlace_aviso_legal' );
    $politica_cookies       =   cdp_cookies::parameter( 'enlace_politica' );
    $politica_privacidad    =   cdp_cookies::parameter( 'enlace_politica_privacidad' );
    $mas_informacion        =   cdp_cookies::parameter( 'enlace_mas_informacion' );
	$desarrollado = cdp_cookies::parameter( 'desarrollado' );
	$desarollador = cdp_cookies::parameter( 'desarrollador' );
?>
<div class="cdp-cookies-admin">
    <h1>Configuración de Asesor de Cookies</h1>
    <p>
        <a href="javascript:;" class="cdp-cookies-bot-instrucciones">[Ocultar/Mostrar instrucciones]</a>
        &nbsp;&nbsp;&nbsp;
        Plugin creado por <a href="http://www.3comunicacion.com/">3com</a>
    </p>
    <div class="cdp-cookies-instrucciones">
        <p>Placeholder text</p>
    </div>
    <form>
            <!-- Texto del aviso -->
            <div class="cdp-cookies-grid">
                    <div class="cdp-cookies-4c">
                            <label class="padv10"><b>Aviso</b> mostrado al visitante</label>
                    </div>
                    <div class="cdp-cookies-5c">
                        <textarea name="texto_aviso" id="texto_aviso" rows="6"><?php echo cdp_cookies::parameter( 'texto_aviso' );?></textarea>
                    </div>
            </div>

            <!-- Guardar -->
            <div class="cdp-cookies-grid">
                    <div class="cdp-cookies-4c">
                            &nbsp;
                    </div>
                    <div class="cdp-cookies-5c">
                            <a href="javascript:;" class="cdp-cookies-boton azul cdp-cookies-guardar">Guardar</a>
                    </div>
            </div>

            <!-- Mensajes de error y avisos -->
            <div class="cdp-cookies-grid">
                    <div class="cdp-cookies-mensajes"></div>
            </div>

    </form>
    
    <div class="box-content">
        <p id="cdp-cookies-contenido-footer">
            <?php 
                echo cdp_cookies::construct_footer_string($mapa_web, $aviso_legal, $politica_cookies, $politica_privacidad,false,$desarrollado,$desarollador);            
            ?>
        </p>
    </div>
    
    <p>Placeholder Text</p>

</div>