/*
 * Context
 * Context is something like set of variables.
 * 
 * Every function has its local context (variables, which are declared in the
 * function) and upper context (the context, where the function is declared).
 * 
 * The most upper context is the global context.
 * 
 * The context of the function is determined during declaration. It is not changed
 * dynamically (by default, but it is possible to change it)
 */
 
var x = 5; // global context variable

function a() {
    var x = 10; // local context variable
    b();
}

function b() {
    console.log(x);
}

b(); // writes 5. Upper context is the global context.
a(); // writes 5. Upper context is still global context. It is determined during declaration.
