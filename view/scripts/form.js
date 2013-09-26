$('document').ready(function(){
  var chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase()); 
    if(chrome) {
      console.log('user agent is Google Chrome');
      document.getElementById('description').innerHTML = 'Bitte wählen Sie den hochzuladenden Ordner aus:';
    } else {
      console.log('user agent is not Google Chrome');
      document.getElementById('description').innerHTML = 'Bitte wählen Sie die hochzuladenden tex-Dateien aus:'
    }

    function handleFileSelect(evt) {
      var files = evt.target.files; // FileList object

      var request = new XMLHttpRequest();
      
      // create FormData object
      var data = new FormData();

      request.open('POST', 'http://sopra.ex-studios.net:8080/mms/import/modules', true);

      // files is a FileList of File objects. List some properties.
      var output = [];
      for (var i = 0, f; f = files[i]; i++) {
        if(f.type == "text/x-tex") {
          output.push('<li><strong>', escape(f.name), '</strong> (', f.type || 'n/a', ') - ',
            f.size, ' bytes, last modified: ',
            f.lastModifiedDate.toLocaleDateString(), '</li>');
          data.append('file', f);
        }
      }
      // upload data
      request.send(data);
      document.getElementById('list').innerHTML = '<ul>' + output.join('') + '</ul>';
  }

  document.getElementById('files').addEventListener('change', handleFileSelect, false);
});