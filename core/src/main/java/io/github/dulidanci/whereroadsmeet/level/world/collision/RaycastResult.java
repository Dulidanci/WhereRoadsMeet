package io.github.dulidanci.whereroadsmeet.level.world.collision;

import com.badlogic.gdx.math.GridPoint3;
import io.github.dulidanci.whereroadsmeet.level.block.Block;
import io.github.dulidanci.whereroadsmeet.level.world.Axis;

public record RaycastResult(GridPoint3 targetCoords, Block targetBlock, Axis side) {
}
