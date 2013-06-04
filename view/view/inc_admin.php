<h4>Administration</h4>
	  	<div class="alert alert-info">
		  	<strong>Hallo Username</strong>
  		</div>
	  	<div class="alert">
		  <button type="button" class="close" data-dismiss="alert">×</button>
		  <strong>Letzte Bearbeitung von:</strong> Prof. Dr. Dr. Dadam am 12.12.2012 23:00h.
		</div>

	  	<a href="#btn-freigeben" id="btn-freigeben" class="btn btn-success" ><?php echo $typ; ?> freigeben</a><br /><br />
	  	<div id="alert-freigeben" class="alert alert-success">
		  	<?php echo $typ; ?> wirklich freigeben?<br/><br/>
		  	<p>
			  	<button class="btn btn-mini btn-success" type="button">Ja</button>
			  	<button class="btn btn-mini" type="button">Abrechenn</button>
			 </p>
  		</div>

	  	<a href="#" id="btn-bearbeiten" class="btn btn-warning" ><?php echo $typ; ?> bearbeiten</a><br /><br />
	  	<div id="alert-bearbeiten" class="alert alert-warning">
		  	<?php echo $typ; ?> wirklich bearbeiten?<br/><br/>
		  	<p>
			  	<a href="index.php?page=create&name=<?php echo $typ ?>&action=Bearbeiten" class="btn btn-mini btn-warning">Ja</a>
			  	<button class="btn btn-mini" type="button">Abrechenn</button>
			 </p>
  		</div>

  		<a href="#btn-arch" id="btn-arch" class="btn btn-info" ><?php echo $typ; ?> archivieren</a><br /><br />
	  	<div id="alert-arch" class="alert alert-info">
		  	<?php echo $typ; ?> wirklich archivieren?<br/><br/>
		  	<p>
			  	<button class="btn btn-mini btn-info" type="button">Ja</button>
			  	<button class="btn btn-mini" type="button">Abrechenn</button>
			 </p>
  		</div>

	  	<a  href="#btn-loeschen" id="btn-loeschen" class="btn btn-danger" ><?php echo $typ; ?> l&ouml;schen</a><br /><br />
	  	<div id="alert-loeschen" class="alert alert-danger">
		  	<?php echo $typ; ?> wirklich l&ouml;schen?<br/><br/>
		  	<p>
			  	<button class="btn btn-mini btn-danger" type="button">Ja</button>
			  	<button class="btn btn-mini" type="button">Abrechen</button>
			 </p>
  		</div>

  		<a href="#btn-deadline" id="btn-deadline" class="btn btn-inverse" ><?php echo $typ; ?> Deadline setzen</a><br /><br />
	  	<div id="alert-deadline" class="alert alert-inverse">
		  	<?php echo $typ; ?> Deadline wirklich setzen?<br/><br/>
		  	<p>
			  	<button class="btn btn-mini btn-inverse" type="button">Ja</button>
			  	<button class="btn btn-mini" type="button">Abrechen</button>
			 </p>
  		</div>