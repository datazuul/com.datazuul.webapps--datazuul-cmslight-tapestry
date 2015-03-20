function markCMSArea() {
	return insert("<!-- PIXOCMS-START -->", "<!-- PIXOCMS-ENDE -->");
}
function demarkCMSArea() {
	ausgabe = replace(document.forms[0].elements[1].value, "<!-- PIXOCMS-START -->", "");
    document.forms[0].elements[1].value = ausgabe;
	ausgabe = replace(document.forms[0].elements[1].value, "<!-- PIXOCMS-ENDE -->", "");
    document.forms[0].elements[1].value = ausgabe;
}

function insert(aTag, eTag) {
  var input = document.forms[0].elements[1];
  input.focus();
  /* for Internet Explorer */
  if(typeof document.selection != 'undefined') {
    /* insert formatting code */
    var range = document.selection.createRange();
    var insText = range.text;
    range.text = aTag + insText + eTag;
    /* adjust cursor position */
    range = document.selection.createRange();
    if (insText.length == 0) {
      range.move('character', -eTag.length);
    } else {
      range.moveStart('character', aTag.length + insText.length + eTag.length);      
    }
    range.select();
  }
  /* for gecko-based browsers */
  else if(typeof input.selectionStart != 'undefined')
  {
    if(input.selectionStart == input.value.length || input.selectionStart == input.selectionEnd) {
      alert("Bitte einen CMS-Bereich markieren.");
      return false;
    }
    /* insert formatting code */
    var start = input.selectionStart;
    var end = input.selectionEnd;
    var insText = input.value.substring(start, end);
    input.value = input.value.substr(0, start) + aTag + insText + eTag + input.value.substr(end);
    /* adjust cursor position */
    var pos;
    if (insText.length == 0) {
      pos = start + aTag.length;
    } else {
      pos = start + aTag.length + insText.length + eTag.length;
    }
    input.selectionStart = pos;
    input.selectionEnd = pos;
  }
  /* for all others */
  else
  {
    /* get insertion position */
    var pos;
    var re = new RegExp('^[0-9]{0,3}$');
    while(!re.test(pos)) {
      pos = prompt("Einfuegen an Position (0.." + input.value.length + "):", "0");
    }
    if(pos > input.value.length) {
      pos = input.value.length;
    }
    /* insert formatting code */
    var insText = prompt("Bitte geben Sie den zu formatierenden Text ein:");
    input.value = input.value.substr(0, pos) + aTag + insText + eTag + input.value.substr(pos);
  }
}

function replace(string,suchen,ersetzen)
{
  ausgabe = "" + string;
  while (ausgabe.indexOf(suchen)>-1) {
    pos= ausgabe.indexOf(suchen);
    ausgabe = "" + (ausgabe.substring(0, pos) + ersetzen +
    ausgabe.substring((pos + suchen.length), ausgabe.length));
  }
  return ausgabe;
}