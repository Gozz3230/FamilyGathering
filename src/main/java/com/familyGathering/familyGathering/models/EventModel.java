package com.familyGathering.familyGathering.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class EventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long eventId;

    String eventName;
    LocalDateTime dateOfEvent;
    String organizer;
    @ManyToOne
    @JoinColumn(name = "familyId")
    FamilyModel family;


    @ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinTable(
            name = "events_to_family_members",
            joinColumns = {@JoinColumn(name = "eventId")},
            inverseJoinColumns = {@JoinColumn(name="memberId")}
    )
    Set<FamilyMemberModel> eventAttendees = new HashSet<>();


//      Chat GPT helper methods
//    public void addFamilyMember(FamilyMemberModel member) {
//        eventAttendees.add(member);
//        member.getMyFamilyEvents().add(this);
//    }
//
//    public void removeFamilyMember(FamilyMemberModel member) {
//        eventAttendees.remove(member);
//        member.getMyFamilyEvents().remove(this);
//    }

    protected EventModel(){};

    public EventModel(String eventName, LocalDateTime dateOfEvent, String organizer, Long idOfFamily) {
        this.eventName = eventName;
        this.dateOfEvent = dateOfEvent;
        this.organizer = organizer;
        if (idOfFamily != null){
            FamilyModel familyModel = new FamilyModel();
            familyModel.setFamilyIdId(idOfFamily);
            this.family = familyModel;

        }
//        this.eventAttendees = new ArrayList<FamilyMemberModel>();
    }

    boolean addFamilyMemberToEvent(FamilyMemberModel member){
        if (member == null){
            return false;
        }
        boolean added = eventAttendees.add(member);

        if(added){
            member.getMyFamilyEvents().add(this);
        }
        return added;
    }

    boolean removeFamilyMemberFromEvent(FamilyMemberModel member){
        if (member == null){
            return false;
        }

        boolean removed = eventAttendees.remove(member);
        if (removed) {
            member.getMyFamilyEvents().remove(this);
        }

        return removed;


    }

    boolean changeDateOfEvent(LocalDateTime newDate){
        LocalDateTime now = LocalDateTime.now();
        if (newDate.isBefore(now)){
            return false;
        }else{
            this.dateOfEvent = newDate;
            return true;
        }
    }




}
