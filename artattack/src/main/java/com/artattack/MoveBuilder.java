package com.artattack;

import java.util.List;

public interface MoveBuilder {
    void setName(String name);
    void setDescription(String description);
    void setPower(int power);
    void setHealAmount(int healAmount);
    void setActionPoints(int actionPoints);
    void setAttackArea(List<Coordinates> attackArea);
    void setHealArea(List<Coordinates> healArea);
}
