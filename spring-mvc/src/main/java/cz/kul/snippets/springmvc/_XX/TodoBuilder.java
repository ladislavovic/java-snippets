package cz.kul.snippets.springmvc._XX;

public class TodoBuilder {
    
    private Todo todo;

    public TodoBuilder() {
        todo = new Todo();
    }
    
    public TodoBuilder id(Long id) {
        todo.setId(id);
        return this;
    }
    
    public TodoBuilder title(String name) {
        todo.setTitle(name);
        return this;
    }
    
    public Todo build() {
        return todo;
    }

}
