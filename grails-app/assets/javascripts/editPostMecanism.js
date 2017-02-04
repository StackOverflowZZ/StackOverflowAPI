/**
 * Created by Pierre on 23/01/2017.
 */

$(document).ready(function(){
    $(".comment_edition.edit_btn").click(function(){
        toggleEdition($(this),".comment_edition");
    });

    $(".answer_edition.edit_btn").click(function(){
        toggleEdition($(this),".answer_edition");
    });
});

function toggleEdition (elementBtn, classHTML){
    var block = elementBtn.closest(classHTML+".editable_post_block");
    var submitBtn =block.find(classHTML+".btn_to_display");
    var display = block.find(classHTML+".editable_display");

    if(display.attr("readonly")=="readonly"){
        display.attr("readonly",null)
            .removeClass("editable_display_no_border");
    }else{
        display.attr("readonly","readonly")
            .addClass("editable_display_no_border")
    }
    submitBtn.toggleClass("hidden");
}