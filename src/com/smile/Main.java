package com.smile;

import com.smile.util.mouse.algorithms.EaseMouse;
import com.smile.util.mouse.paint.DrawMouseEvent;
import com.smile.settings.Locations;
import com.smile.settings.NPCTask;
import com.smile.settings.States;
import com.smile.tasks.Inventory.BuryBones;
import com.smile.tasks.Inventory.DropItems;
import com.smile.tasks.combat.chickens.AttackChickens;
import com.smile.tasks.combat.chickens.TrainAttackChicken;
import com.smile.tasks.combat.chickens.TrainDefenceChicken;
import com.smile.tasks.combat.chickens.TrainStrengthChicken;
import com.smile.tasks.combat.cows.AttackCows;
import com.smile.tasks.combat.cows.TrainAttackCows;
import com.smile.tasks.combat.cows.TrainDefenceCows;
import com.smile.tasks.combat.cows.TrainStrengthCows;
import com.smile.tasks.combat.worldhop.HopWorld;
import com.smile.tasks.general.Logout;
import org.dreambot.api.Client;
import org.dreambot.api.methods.interactive.Players;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.skills.SkillTracker;
import org.dreambot.api.methods.skills.Skills;
import org.dreambot.api.methods.world.Worlds;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.impl.TaskScript;

import java.awt.*;

@ScriptManifest(category = Category.COMBAT, name = "Trade Restrictions Lite", author = "smile", version = 0.4)
public class Main extends TaskScript {
    public static Locations location = Locations.NONE;
    public static NPCTask npc = NPCTask.NONE;
    public static States state = States.NOTHING;
    private long timeStart, timeRan;

    @Override
    public void onStart() {
        super.onStart();
        log("Setting mouse algorithm to EaseMouse.");
        Client.getInstance().setMouseMovementAlgorithm(new EaseMouse());
        //Client.getInstance().setMouseMovementAlgorithm(new WindMouse());
        log("Done!");
        SkillTracker.start();
        timeStart = System.currentTimeMillis();
        addNodes(
                new AttackChickens(),
                new AttackCows(),
                new TrainAttackCows(),
                new TrainDefenceCows(),
                new TrainStrengthCows(),
                new TrainAttackChicken(),
                new TrainStrengthChicken(),
                new TrainDefenceChicken(),
                new Logout(),
                new BuryBones(),
                new DropItems(),
                new HopWorld()
        );

    }
    public final String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }

    @Override
    public void onPaint(Graphics2D g) {
        timeRan = System.currentTimeMillis() - timeStart;
        super.onPaint(g);
        /*
        g.setColor(new Color(80,255,140,255));
        g.drawRect(8,17,165,125);
        g.setColor(new Color(0,0,0,175));
        g.fillRect(10,19,163,123);

         */

        g.setColor(Color.GREEN);
        g.drawString("sTradeRestriction Lite v" + getVersion(),12,33);
        g.drawString("Runtime: " + formatTime(timeRan),12,48);
        if(Players.localPlayer() != null) {
            g.drawString("Location: " + location.getName(), 12,65);
            g.drawString("State: " + state.getState() + " - " + npc.getName() ,12,77);
            g.drawString("Animation: " + Players.localPlayer().getAnimation(),12,89);

            g.drawString("HP: " + Skills.getRealLevel(Skill.HITPOINTS) + " (+" + SkillTracker.getGainedLevels(Skill.HITPOINTS) + ")" + " [" + SkillTracker.getGainedExperiencePerHour(Skill.HITPOINTS) + "]",12,110);
            g.drawString("Attack: " + Skills.getRealLevel(Skill.ATTACK) + " (+" + SkillTracker.getGainedLevels(Skill.ATTACK) + ")" + " [" + SkillTracker.getGainedExperiencePerHour(Skill.ATTACK) + "]",  12,122);
            g.drawString("Strength: " + Skills.getRealLevel(Skill.STRENGTH) + " (+" + SkillTracker.getGainedLevels(Skill.STRENGTH) + ")" + " [" + SkillTracker.getGainedExperiencePerHour(Skill.STRENGTH) + "]" ,12,134);
            g.drawString("Defence: " + Skills.getRealLevel(Skill.DEFENCE) + " (+" + SkillTracker.getGainedLevels(Skill.DEFENCE) + ")" + " [" + SkillTracker.getGainedExperiencePerHour(Skill.DEFENCE) + "]",12,146);
            g.drawString("Total Level: " + Skills.getTotalLevel(),12, 158);
            g.drawString("World: " + Worlds.getCurrentWorld(),12,170);
            //DrawMouseEvent.getInstance().setCursorColor(Color.);
            //DrawMouseEvent.getInstance().setTrailColor(new Color(199,36,177));
            DrawMouseEvent.getInstance().setTrailColor(Color.red);
            DrawMouseEvent.getInstance().drawTrail(g);
            DrawMouseEvent.getInstance().drawPlusMouse(g);
        }
    }
}
