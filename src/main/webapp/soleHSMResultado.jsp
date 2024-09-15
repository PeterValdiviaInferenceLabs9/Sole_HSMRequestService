<%-- 
    Document   : soleHSMResultado
    Created on : 30 ene. 2023, 1:17:14 p. m.
    Author     : Luis D
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sole HSM Satisfactorio!</title>
        <link rel="stylesheet" type="text/css" href="css/style2.css">
        <script type="text/javascript" src="js/soleResultadosLogic.js"></script>
    </head>
    <body>
        <div id="general">
            <div id="bloque1">
                <h2 class="form_title" id="rs">Envio Satisfactorio!</h2>
                <h2 class="form_title4">Datos de la solicitud HSM enviada:</h2>
                <h2 class="form_title3" id="agent">Cuenta de Agente: </h2>
                <h2 class="form_title3" id="ani">Teléfono de Cliente: </h2>
                <h2 class="form_title3" id="template">Plantilla: </h2>
                <h2 class="form_title3" id="result">Resultado: </h2>

                <script>
                    var objectAgent = document.getElementById("agent");
                    var objectANI = document.getElementById("ani");
                    var objectTemplate = document.getElementById("template");
                    var addImg = document.getElementById('bloque1');
                    var objectResult = document.getElementById('result');

                    objectAgent.innerHTML += getParamByName('agent');
                    objectANI.innerHTML += getParamByName('ani');
                    objectTemplate.innerHTML += getParamByName('template');
                    objectResult.innerHTML += getParamByName('rs');

                    if (!getParamByName('agent') || !getParamByName('ani') || !getParamByName('template') || !getParamByName('rs')) {
                        let temp = document.getElementById("rs");
                        temp.innerHTML = "Envio Fallido!";
                        addImg.innerHTML += '<br><img src="https://web-prd.inferencelabs9.com/HSMRequestService-1.0/css/img/fail.png" width="200" height="200" alt="150"/>';
                    } else {
                        addImg.innerHTML += '<br><img src="https://web-prd.inferencelabs9.com/HSMRequestService-1.0/css/img/check.png" width="200" height="200" alt="150"/>';
                    }
                </script>
                <br>

            </div>
            <div id="bloque2">
                <input type="submit" class="form_submit" value="Volver a la pagina anterior" onclick="location.href = 'https://web-prd.inferencelabs9.com/HSMRequestService-1.0/soleServicioHSM.jsp'">
            </div>
        </div>	
    </body>
</body>
</html>
