/**
 * (1) Assign event to the DOM object
 */
function foo() {
  log.console("FOO function");
}
document.getElementById("b").onclick = function() {
  foo();
};
