$(document).ready(function(){
	var formValues = []

	var pageOne = $('#form-page-one');
	var pageTwo = $('#form-page-two');
	var pageThree = $('#form-page-three');
	var pageFour = $('#form-page-four');

	// console.log(pageOne);
	// pageOne.hide();
	pageTwo.hide();
	pageThree.hide();
	pageFour.hide();


	$('#login').click(function(){
		pageOne.hide();
		pageTwo.show();
	});

	$('#trip_type_next').click(function(){
		// pageOne.hide();
		pageTwo.hide();
		pageThree.show();
	});

	$('#mode_choice_next').click(function(){
		// pageOne.hide();
		pageThree.hide();
		pageFour.show();
	});

	$('#submit_another').click(function(){
		pageFour.hide();
		pageTwo.show();
	});


	$('#btn-arrive').click(function(){
		setFormValue('trip_type', 'arrive');
	});

	$('#btn-depart').click(function(){
		setFormValue('trip_type', 'depart');
	})

	$('#btn-today').click(function(){
		setFormValue('trip_date', 'today');
	})

	$('#btn-yesterday').click(function(){
		setFormValue('trip_date', 'yesterday');
	})

	$('.choice').click(function(){
		var id = $(this).attr('id').split("-")[1];
		setFormValue('mode_choice', id);
	})



	function setFormValue(key, value) {
		formValues[key] = value;
		console.log(formValues)
	}
})