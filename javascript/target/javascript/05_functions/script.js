/*
 * - in the declaration can be whatever. The important thing is, what is
 * in the context, when the function is running. 
 * 
 */

/**
 * Inner functions
 * 
 * TODO context may be something different
 * 
 * Functions can be declared in the function. Then their upper context is
 * context of the outer function.
 */
function a() {
	function aa() {
	  consle.log(x);	
	}
	var x = 10;
	aa();
}
a(); // write 10. It is variable from the context of the function a().


//
// this - refer to the owning object of the function
//
//


//
// This is very interesting
//
this.aaa = "global obj";
var a = {
    
    aaa : "obj a",
    
    fce : function() {
        console.log(this.aaa);
    }
    
};
var b = a.fce;
a.fce(); // obj a
b(); // global obj
