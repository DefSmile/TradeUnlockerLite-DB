package com.smile.tasks.combat.cows;

import com.smile.main;
import com.smile.settings.Locations;
import com.smile.settings.NPCTask;
import com.smile.settings.States;
import org.dreambot.api.input.Mouse;
import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.NPCs;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.TaskNode;
import org.dreambot.api.utilities.StatusBar;
import org.dreambot.api.wrappers.interactive.NPC;

public class AttackCows extends TaskNode {
    @Override
    public boolean accept() {
        return (Skills.getRealLevel(Skill.ATTACK) <= 45
                || Skills.getRealLevel(Skill.STRENGTH) <= 45
                || Skills.getRealLevel(Skill.DEFENCE) <= 45)
                && !Players.localPlayer().isInCombat();
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public int execute() {
        NPC cow = NPCs.closest(c -> c != null && c.getName().equals("Cow") && !c.isInCombat() && c.hasAction("Attack") && c.canAttack());
        if (Locations.COW_PEN.getArea().contains(Players.localPlayer())) {
            if (cow != null) {
                main.npc = NPCTask.COW;
                main.location = Locations.COW_PEN;
                if (!cow.isInteractedWith() && !Players.localPlayer().isInCombat()) {
                    sleep(500,800);
                    if(cow.interact("Attack")) {
                        main.state = States.ATTACKING;
                        Mouse.moveMouseOutsideScreen();
                        StatusBar.info("Attacking cow", false);
                    }
                } else {
                    sleepWhile(() -> Players.localPlayer().isAnimating(), cow::exists,5000,5);
                }
            }
        } else {
            if (!Locations.COW_PEN.getArea().contains(Players.localPlayer()) && !Players.localPlayer().isInCombat()) {
                StatusBar.info("Walking to Cow pen", false);

                Walking.walk(Locations.COW_PEN.getArea().getRandomTile());
            }
        }
        return Calculations.random(1000,1200);
    }
}