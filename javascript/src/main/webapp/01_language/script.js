/**
 * Logging. Browsers have build-in consoles, javascript can
 * log into them.
 */
console.log("LOGGING:");
console.log("ahoj");
console.log("");

/**
 * Semicolons are optional. But there is a trap. There is five tokens:
 *  "(", "[", "/", "+", "-". If they are immediately after a statement
 *  on the next line, they are connected to the statement.
 *  So I think it is better to use semicolons. But you keep in mind
 *  that new line is obviously end of statement.
 *  
 *  (1) logs 15
 *  
 *  (2) logs undefined
 */
// (1)
console.log("SEMICOLON:");
a = 10
+5
console.log(a);

// (2)
var f = function() {
    return
    10
};
console.log(f());

/**
 * You can break up a string to the next line by \ 
 */
console.log("BREAK UP");
console.log("Hello \
world");

/**
 * var keyword is for variable declaration. It is optional, but
 * there is the difference, when you are not in global context.
 * 
 * When you declare variable without var, then is search up the
 * context chain up to finds a variable or hit the global scope.
 * Then create a variable. So the (1) use the variable from the
 * global scope and (2) creates a new variable in the global scope. 
 */
console.log("VAR");
var varFoo = 10;
var varBar = 10;
var varFunction = function() {
	var varFoo = 20;
	varBar = 20; // (1)
	varBaz = 20; // (2)
	console.log("varFoo: " + varFoo);
	console.log("varBar: " + varBar);
	console.log("varBaz: " + varBaz); 
};
varFunction();
console.log("varFoo: " + varFoo);
console.log("varBar: " + varBar); 
console.log("varBaz: " + varBaz);

/**
 * Javascript has a dynamic types. This is possible:
 */
var a = 5;   // it is Number
a = "ahoj";  // now it is String

/**
*/
console.log("ARRAYS:");
var arr1 = new Array(); // 1st way of creating array
arr1[0] = "BMW";
arr1[1] = "Mercedes";
var arr2 = new Array("BMW", "Mercedes"); // 2nd way
var arr3 = ["BMW", "Mercedes"]; // 3rd way

/**
 * Functions
 */
console.log("FUNCTIONS:");
function greeting() {
  console.log("Hello!");	
}
greeting();
var a = greeting;
a();

/** 
 * Typeof operator
 * 
 * The typeof operator returns a data type of operand. It returns a string.
 * it can return six possible values: object, boolean, function, number
 * string, undefined
 */
console.log("TYPEOF");
typeof abcd; // returns "undefined"
typeof function() {}; // returns "function"
typeof "hello"; // returns string
typeof new String("hello"); // returns "object"

/**
 * null vs undefined
 * 
 * null - there is no value
 * undefined - there is no such variable / property / argument
 * 
 * But be careful. The undefined can be assigned to variable!
 */
console.log("NULL VS UNDEFINED");
var a;
console.log(a === undefined); // true
console.log(abcde === undefined); // true


//
// Arrays
//

