<?php

$name = $_GET['name'];
$action = $_GET['action'];

?>


<div class="container-fluid">
	<div class="row-fluid">
		<ul class="span 12 breadcrumb">
		<li><a href="index.php">Home</a> <span class="divider">/</span></li>
		<li class="active"><?php echo $name;?> <?php echo $action;?></li>
		</ul>
	</div>
</div>

<div class="container-fluid">
	<div class="row-fluid">
		<div class="span2">

		</div>
		<div class="span7">
			<h1><?php echo $name;?> <?php echo $action;?></h1>
			<p class="lead"> Hier k&ouml;nnen Sie das <?php echo $name;?> <?php echo $action;?>.</p>

			<form>
				  <fieldset>
				    <legend><?php echo $name; ?> Daten</legend>
				    <label>Name</label>
				    <input type="text" placeholder="<?php echo $name;?> Name">
				    <span class="help-block">Bitte w&auml;len Sie einen Namen.</span>

				    <label>Typ w&auml;hlen</label>
				    <select>
					  <option>Handbuch</option>
					  <option>Modul</option>
					  <option>Vorlesung</option>
					  <option>Tutorium</option>
					  <option>&Uuml;bung</option>
					</select>
					<span class="help-block">Bitte w&auml;len Sie einen Typ.</span>

					<label>Eltern element w&auml;hlen</label>
				    <select multiple="multiple">
					  <option>Analysis</option>
					  <option>Informatik</option>
					  <option>Analysis</option>
					  <option>Informatik</option>
					</select>


				  </fieldset>
				  <fieldset>
					<legend><?php echo $name; ?> Beschreibung</legend>
					<label>Beschreibung</label>
					<textarea rows="5" cols="10"></textarea>
					<label>Beschreibung</label>
					<textarea rows="5" cols="10"></textarea>
					<label>Beschreibung</label>
					<textarea rows="5" cols="10"></textarea>
					<label>Beschreibung</label>
					<textarea rows="5" cols="10"></textarea>
				  </fieldset>
				  <fieldset>
				  <legend>Absenden</legend>
				  	<button type="submit" class="btn">Submit</button>
				  </fieldset>
			</form>


		</div>
	</div>
</div>