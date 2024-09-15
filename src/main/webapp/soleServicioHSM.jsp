<%-- 
    Document   : portalHSMRequestSole
    Created on : 15 dic. 2022, 12:37:48 p. m.
    Author     : Luis D
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Sole Servicio HSM</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <script type="text/javascript" src="js/soleServicioLogic.js"></script>
    </head>
    <body>
        <div id="general">
            <form action="SolicitudHSMSole" method="get">
                <div id="ofertas"> 
                    <h2 class="form_title">Solicitud HSM Sole</h2>
                    <div class="form_container">
                        <div class="form_group">
                            <input type="number" id="name" class="form_input" name="aniClient" placeholder=" ">
                            <label for="name" class="form_label">Teléfono (Ejem 51XXXXXXXXX)</label>
                            <p></p>
                            <span class="form_line"></span>
                        </div>
                        <div class="form_group">
                            <input type="text" id="user" class="form_input" name="agentUser" placeholder=" ">
                            <label for="name" class="form_label">Cuenta de Agente</label>
                            <span class="form_line"></span>
                            <script>
                                document.getElementById("user").value = getParamByName("user_name");
                            </script>
                        </div>
                        <div class="form_group">
                            <br>
                            <label>Lista desplegable</label>
                            <select id="templateSelected" name="templateName" onchange="updateTemplate()">
                                <option disabled selected value> -- selecciona una opción -- </option>  
                                <option value="cc_sole_taller_hsm11_custom">cc_sole_taller_hsm11_custom</option>
                                <option value="cc_sole_saludo_hsm11_customs">cc_sole_saludo_hsm11_customs</option>
                                <option value="cc_sole_noatencion_hsm11_custom">cc_sole_noatencion_hsm11_custom</option>
                                <option value="cc_sole_confirmacion_hsm11_custom">cc_sole_confirmacion_hsm11_custom</option>
                                <option value="cc_sole_internado_hsm11_custom">cc_sole_internado_hsm11_custom</option>
                                <option value="cc_sole_mantenimiento_anticipado_hsm11_custom">cc_sole_mantenimiento_anticipado_hsm11_custom</option>
                                <option value="cc_sole_bienvenida_hsm11_custom">cc_sole_bienvenida_hsm11_custom</option>
                                <option value="cc_sole_ei_no_entrega_hsm11_custom">cc_sole_ei_no_entrega_hsm11_custom</option>
                            </select>
                        </div>
                        <input type="submit" class="form_submit" onclick="this.form.submit(); this.disabled = true; this.value = 'Sending…';" value="Solicitar">
                    </div>

                </div>
                <div id="novedades">
                    <h2 class="form_title2">Campos de plantilla</h2>
                </div>
                <div id="body">
                    <h2 class="form_title2">Texto de Plantilla</h2>
                    <br></br>
                    <textarea id="templateContent"></textarea>
                </div>
            </form>
        </div>
    </body>
</html>
