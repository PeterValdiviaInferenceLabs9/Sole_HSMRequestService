/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
function getParamByName(param_name) {
    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var rs = urlParams.get(param_name);
    return rs;
}

function lookOnListTemplate(templateInput) {
    var templatesAvailable = [
        ['cc_sole_taller_hsm11_custom', 4, 'Hola {{1}},\nTe saluda {{2}} 💻, ejecutiva de SOLE. Nos comunicamos por su {{3}} que dejo internado en nuestro taller para realizar el servicio de {{4}}. Para mayor detalle por favor presiona sobre el botón INICIAR CONVERSACIÓN y enseguida estaremos contigo.'],
        ['cc_sole_saludo_hsm11_customs', 2, 'Buen día  *{{1}}*😊,\nTe saluda *{{2}}* de SOLE 💙. Te contactaba para poder programar tu visita técnica 🧑🏻‍🔧.'],
        ['cc_sole_noatencion_hsm11_custom', 3, 'Hola *{{1}}*,\nLamentamos que no se haya concretado la visita técnica ⚒️ programada el *{{2}}* con N° *{{3}}*. Para reprogramar tu visita por favor presiona sobre el botón *INICIAR CONVERSACIÓN* y enseguida estaremos contigo.'],
        ['cc_sole_confirmacion_hsm11_custom', 2, 'Hola *{{1}}*,\nTe recordamos que tienes programada tu visita técnica ⚒️ para el día de mañana *{{2}}*, si deseas cancelar o reprogramar; por favor presiona sobre el botón *INICIAR CONVERSACIÓN* y enseguida estaremos contigo.'],
        ['cc_sole_internado_hsm11_custom', 3, 'Hola *{{1}}*,\n💙 SOLE le informa que su *{{2}}* ingreso a nuestro taller con N° *{{3}}* y será revisado por nuestros técnicos especialistas. Máximo en 48 horas nos comunicaremos por este mismo canal para indicarle los detalles de lo encontrado por nuestros técnicos. Para mayor detalle por favor presiona sobre el botón *INICIAR CONVERSACIÓN* y enseguida estaremos contigo.'],
        ['cc_sole_mantenimiento_anticipado_hsm11_custom', 1, 'Buen día {{1}} 😊,\nTe recordamos que cuando compraste tu producto SOLE 💙, pagaste la mano de obra del mantenimiento anticipado, es por ello te contactamos para programar tu visita técnica 🧑🏻‍🔧. Por favor presiona sobre el botón INICIAR CONVERSACIÓN y enseguida estaremos contigo.'],
        ['cc_sole_bienvenida_hsm11_custom', 2, 'Hola *{{1}}*,\nBuen día, espero que usted y sus seres queridos se encuentren bien. Le saluda *{{2}}* 💻, ejecutivo de SOLE.'],
        ['cc_sole_ei_no_entrega_hsm11_custom', 2, 'Hola *{{1}}* 👋:\nHemos tenido que modificar la fecha de entrega de tu compra.\nTe pedimos disculpas por las molestias que hayamos podido ocasionar con este retraso 🙌.\n🗓️ La nueva fecha de entrega de tu pedido será el día *{{2}}*\nAgradecemos tu comprensión.\n*Equipo Atención al Cliente SOLE 💙*']
    ];

    let pivot = 0;
    let numFieldsAva;
    let textTemplate;

    while (true) {
        if (templatesAvailable[pivot][0].trim() === templateInput.trim()) {
            numFieldsAva = templatesAvailable[pivot][1];
            textTemplate = templatesAvailable[pivot][2];
            break;
        } else {
            pivot = pivot + 1;
        }
    }
    return temp = [templateInput, numFieldsAva, textTemplate];
}

function updateTemplate() {
    //alert("1");
    var chosenTemplate = document.getElementById('templateSelected');
    var value_chosenTemplate = chosenTemplate.value;
    var containerFields = document.getElementById('novedades');
    var containerTextTemplate = document.getElementById('templateContent');
    //var fieldsNum;

    containerFields.innerHTML = '<h2 class="form_title2">Campos de plantilla</h2>';

    let tempArray = lookOnListTemplate(value_chosenTemplate);
    let numFields = tempArray[1];

    for (let i = 1; i <= numFields; i++) {
        containerFields.innerHTML += '<input type="text" class="form_input_fields" name="' + 'field' + i + '" placeholder="Campo' + i + '"><p></p><span class="form_line">';
    }

    let textTemp = tempArray[2];
    containerTextTemplate.innerHTML = textTemp;
}