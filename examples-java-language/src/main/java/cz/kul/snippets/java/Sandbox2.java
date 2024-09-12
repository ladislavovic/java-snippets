package cz.kul.snippets.java;

import org.apache.commons.collections.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Sandbox2
{

    public static void main(String[] args)
    {



        Set<String> alreadyCreatedAccordingToMe = new HashSet<>( List.of("SO486238-US01",
            "SO478667-CA01",
            "SO488117-US01",
            "SO486131-US01",
            "SO488423-US01",
            "SO485401-CA01",
            "SO478665-CA01",
            "SO488118-US01",
            "SO485401-CA01",
            "SO486131-US01",
            "SO486614-US01",
            "SO478667-CA01",
            "SO475007-US01",
            "SO486134-US01",
            "SO486612-US01",
            "SO486258-US01",
            "SO486146-US01",
            "SO452518-US01",
            "SO473374-CA01",
            "SO488105-US01",
            "SO486608-US01",
            "SO486130-US01",
            "SO486259-US01",
            "SO486130-US01",
            "SO488120-US01",
            "SO486148-US01",
            "SO486135-US01",
            "SO486134-US01",
            "SO486135-US01",
            "SO486487-US01",
            "SO478665-CA01",
            "SO475008-US01",
            "SO486146-US01",
            "SO475006-US01",
            "SO486611-US01",
            "SO486615-US01",
            "SO452518-US01",
            "SO486148-US01",
            "SO488119-US01",
            "SO486606-US01",
            "SO486238-US01",
            "SO467981-US01"));

        Set<String> alreadyCreatedByOoni = Set.of(
            "SO490987-US01",
            "SO452518-US01",
            "SO488105-US01",
            "SO488117-US01",
            "SO488118-US01",
            "SO488119-US01",
            "SO488120-US01",
            "SO486130-US01",
            "SO486134-US01",
            "SO486135-US01",
            "SO486146-US01",
            "SO486148-US01",
            "SO486238-US01",
            "SO486131-US01",
            "SO486606-US01",
            "SO486608-US01",
            "SO486611-US01",
            "SO486612-US01",
            "SO486614-US01",
            "SO486615-US01",
            "SO486258-US01",
            "SO486259-US01",
            "SO486487-US01",
            "SO487027-US01",
            "SO488423-US01",
            "SO467981-US01",
            "SO477585-US01",
            "SO484641-US01",
            "SO475006-US01",
            "SO475007-US01",
            "SO475008-US01",
            "SO480847-US01",
            "SO485401-CA01",
            "SO478665-CA01",
            "SO478667-CA01",
            "SO473374-CA01"
        );

        Set<String> toResend = Set.of("SO484826-US01",
            "SO473084-US01",
            "SO479803-US01",
            "SO479801-US01",
            "SO480816-US01",
            "SO478666-CA01",
            "SO467362-US01",
            "SO479792-US01",
            "SO480814-US01",
            "SO474942-US01",
            "SO480231-US01",
            "SO483968-US01",
            "SO467359-US01",
            "SO480815-US01",
            "SO467361-US01",
            "SO477920-US01",
            "SO480818-US01",
            "SO479802-US01",
            "SO479789-US01",
            "SO481881-US01",
            "SO467363-US01",
            "SO479698-US01",
            "SO469368-US01",
            "SO467360-US01",
            "SO479800-US01",
            "SO437236-US01",
            "SO481870-US01",
            "SO479798-US01",
            "SO481869-US01",
            "SO481868-US01",
            "SO479796-US01",
            "SO479791-US01",
            "SO479797-US01",
            "SO479793-US01",
            "SO474253-CA01",
            "SO480817-US01",
            "SO473375-CA01");

        System.out.println("Intersection between 'toResend' and 'createdByOoni': " + CollectionUtils.intersection(alreadyCreatedByOoni, toResend));

        System.out.println("No of 'already created by me': " + alreadyCreatedAccordingToMe.size());
        System.out.println("No of 'already created by me', which were created by ooni: " + CollectionUtils.intersection(alreadyCreatedByOoni, alreadyCreatedAccordingToMe).size());
        System.out.println("No of 'toResend': " + toResend.size());

    }



}