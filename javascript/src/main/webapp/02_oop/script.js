//
// Javascript is object language. All (except of primitives) in the
// language are Objects - arrays, functions, regexes, ...
//

//
// What the object is in the javascript
//
// It is set of preperties. Property can be object or primitive. It is
// something like HashMap in Java.
var person1 = {
	firstname: "Pepa",
	lastname: "Novak"
};
log.console(person1.firstname); // Pepa
log.console(person1.age); // undefined
person1.age = 30; // you can dynamically add properties
log.console(person1.age); // 30

//
// How to create object in Javascript
//
// 1 new operator
var person = new Object();  
person.name = "Diego";  
person.getName = function(){  
    return this.name;  
}; 

// 2 Literal notation
var person2 = {
    person2.name : "Diego",  
    person2.getName : function(){  
        return this.name;  
    }  
}

// 3 when you create array, function, ...

// 4 Object create TODO


/**
 * (2) Constructors and function definitions
 * 
 * Function, which purpose is to create a new object, is called
 * constructor. Constructor and "common" function does not differ
 * in the syntax. It is only by they purpose (actually every function
 * can be used as a constructor).
 * 
 * Javascript does not have class definitions. It uses functions
 * for that. Constructor == class definition.
 * 
 */
console.log("2 CONSTRUCTORS AND FUNCTION DEFINITIONS");

// (1) This is a constructor and a class definition too
function Animal() {
	this.eat = true;
}

// (2) It create a new instance
var a = new Animal();

/**
 * 3 Inheritance
 * 
 * Javascript use prototype inheritance. In most of programming
 * languages, class inheriths from another class. In the javascript,
 * the object inherits from another object.
 * 
 * Each constructor (actually function) has a property prototype.
 * It says, which object is used as prototype of newly created
 * instance.
 * 
 * If no prototype is explicitly set, the Object.prototype is used.
 */
console.log("3 INHERITANCE");

// (1) Constructor definitions
function Animal() {
	this.eat = true;
}
function Rabbit() {
	this.eat = "Carrot and cabbage only"; // property shading TODO shading???
	this.eatCabbage = true;
}
Rabbit.prototype = new Animal();

// (2) Instances creation
var r1 = new Rabbit();
var r2 = new Rabbit();
console.log(r1.eat); // logs true
console.log(r1.eatCabbage); // logs true
console.log(Object.getPrototypeOf(r1) === Object.getPrototypeOf(r2)); // logs true, both instances has the same prototype

/**
 * 4 The prototype chains of standard objects
 * 
 *                Object               -->   Object.prototype   -->   null
 * Array     -->  Array.prototype      -->   Object.prototype   -->   null
 * Function  -->  Function.prototype   -->   Object.prototype   -->   null
 * 
 */
console.log("4 THE PROTOTYPE CHAINS OF STANDARD OBJECTS");


/**
 * 5 Reading looks up, writing doesn't
 * 
 * When a property is read, like this.prop, the interpreter looks it in the prototype.
 * When a property is assigned, like this.prop = value, then there is no reason to search.
 * The property is written directly into the object (here this).
 */
console.lgo("5 READING LOOKS UP, WRITING DOES NOT");



//
// Javascript - prototype
//
// Every JS object has link to its prototype. It is something like hidden property. (__proto__ in Firefox anch chrome)
// This link probably is not the "prototype" property of function
// A new function call sets the __proto__ of the object to the value of its prototype property.






// http://www.htmlgoodies.com/beyond/javascript/object.create-the-new-way-to-create-objects-in-javascript.html
// http://javascript.info/tutorial/inheritance
// TODO prototype.constructor - what the hell???