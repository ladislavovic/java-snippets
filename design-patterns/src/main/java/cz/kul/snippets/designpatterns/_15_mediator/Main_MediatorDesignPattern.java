package cz.kul.snippets.designpatterns._15_mediator;

/**
 * Sometimes we need a lot of object communicate together. It is not effective to connect
 * all objects with all objects. It is better to create <b>Mediator</b> which handle
 * communication and connect every object only with mediator.
 * 
 * Mediator - middleman in the communication
 * 
 * Colleague - object which needs to communicate
 * 
 * Typical example is a chat room: ChatRoom is a mediator and ChatParticipant is a
 * Colleague. It does not know anything about other participants, but it knows ChatRoom
 * and it is all he needs.
 * 
 */
public class Main_MediatorDesignPattern {

    public Main_MediatorDesignPattern() {
        // TODO Auto-generated constructor stub
    }

}
