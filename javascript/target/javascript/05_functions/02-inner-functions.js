/**
 * Inner functions TODO
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