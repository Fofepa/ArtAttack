package com.artattack.moves;

import java.util.List;

import com.artattack.level.Coordinates;

public interface MoveBuilder {
    void setName(String name);
    void setDescription(String description);
    void setPower(int power);
    void setHealAmount(int healAmount);
    void setActionPoints(int actionPoints);
    void setAttackArea(List<Coordinates> attackArea);
    void setHealArea(List<Coordinates> healArea);
    void setAreaAttack(boolean areaAttack);
    void setAreaHeal(boolean areaHeal);
}
