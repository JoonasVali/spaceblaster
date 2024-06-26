package com.github.joonasvali.spaceblaster.event;

public enum EventType {
  PLAYER_KILLED,
  PLAYER_BORN,
  PLAYER_NO_LONGER_INVINCIBLE,
  ENEMY_KILLED,
  ENEMY_KILLED_BY_ENEMY,
  ENEMY_HIT,
  POWERUP_COLLECTED,
  POWERUP_MISSED,
  POWERUP_CREATED,
  ENEMY_FORMATION_CHANGES_MOVEMENT_DIRECTION,
  VICTORY,
  ROUND_COMPLETED,
  GAME_OVER,
  LOAD_LEVEL,
  PLAYER_NARROWLY_ESCAPED_INCOMING_BULLET,
  START_GAME
}
