REST
=================
  
* There are three ways how to create REST API with spring
  * MVC ModelAndView. It sas not created for REST API. It is tough way how to do REST. 
    You must create views and bind model. Not used for REST now.
  * HTTP message converters. Converters which can convert data to/from http body.
    Usually annotation driven. Quite good choice for REST. 
  * reactive
  
Spring Doc
-----------------
* community-based project, not maintained by the Spring Framework Contributors
* it examine an application at runtime and generate documentation in JSON, YAML and HTML
* The generateed documentation can be complemented using swagger-api annotations.
* probably suppressor of Springfox

Springfox
-----------------
* support OpenAPI 2.0 well. Support of 3.0 does not seems to be reliable
  * support was added 3 years after introducing OpenAPI 3.0 standard
  * there is still only 3.0.0 version, no bugfix
  * small commit frequency
  * documentation seems to be still for 2.x version
* your API in OpenAPI2 format is available at /v2/api-docs. When you use groups then the
  api is available at /v2/api-docs?group=group_name
  
 Documentation generation
 -------------------------
 It is quite tricky how to generate static api documentation. The generatig consists of these steps:
 1. create swagger.json file. It is created by unit test, which runs the application and send HTTP
    request to springfox. It is a workaround but I do not know any better way how to get the file.
    Because of that the whole generating is done during Maven "test" phase.
 2. create asciidoc files by swagger2markdown mavenn plugin
 3. create html and pdf by asciidoctor maven plugin
 