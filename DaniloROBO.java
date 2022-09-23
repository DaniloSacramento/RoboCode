package Exercicio;

import robocode.*;
import java.awt.Color;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.util.*;

public class Crawler extends RateControlRobot {
  
    public void run() {
        
       
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);

        
        while (true) {
            setVelocityRate(5);
            setTurnRateRadians(0);
            execute();
            turnRadarRight(360);
        }
    }
    
    public void onScannedRobot(ScannedRobotEvent e) {
        double potenciaDoTiro = Math.min(2.0, getEnergy());
        double distancia = getHeadingRadians() + e.getBearingRadians();
        double inimigoX = getX() + e.getDistance() * Math.sin(distancia);
        double inimigoY = getY() + e.getDistance() * Math.cos(distancia);
        double posicaoDoInimigo = e.getHeadingRadians();
        double velocidadeDoInimigo = e.getVelocity();
        double altDoCampDeBatalha = getBattleFieldHeight(),larDoCampDeBatalha = getBattleFieldWidth();
        double previsaoX = inimigoX, previsaoY = inimigoY;
        
        previsaoX += Math.sin(posicaoDoInimigo) * velocidadeDoInimigo;
        previsaoY += Math.cos(posicaoDoInimigo) * velocidadeDoInimigo;
        if (previsaoX < 18.0 || previsaoY < 18.0 || previsaoX > larDoCampDeBatalha - 18.0 || previsaoY > altDoCampDeBatalha - 18.0) {
            previsaoX = Math.min(Math.max(18.0, previsaoX), larDoCampDeBatalha - 18.0);
            previsaoY = Math.min(Math.max(18.0, previsaoY), altDoCampDeBatalha - 18.0);
        }
        
        double anguloAbsoluto = Utils.normalAbsoluteAngle(
            Math.atan2(
                previsaoX - getX(), previsaoY - getY()
            )
        );
        
        setTurnRightRadians(distancia / 2 * - 1 - getRadarHeadingRadians());
        setTurnRadarRightRadians(Utils.normalRelativeAngle(distancia - getRadarHeadingRadians()));
        setTurnGunRightRadians(Utils.normalRelativeAngle(anguloAbsoluto - getGunHeadingRadians()));
        fire(potenciaDoTiro);
        
        if (getVelocityRate() > 0){
            setVelocityRate(getVelocityRate() + 1);
        } 
        else {
            setVelocityRate(- 1);
        }

        if (getVelocityRate() > 0 && ((getTurnRate() < 0 && distancia > 0) || (getTurnRate() > 0 && distancia < 0))) {
            setTurnRate(getTurnRate() * -1);
        }
    }
  
    public void onHitByBullet(HitByBulletEvent e) {
        double giroDoRadar = normalRelativeAngleDegrees(e.getBearing() + getHeading() - getRadarHeading());
        setTurnRadarRight(giroDoRadar);
        setTurnLeft(-3);
        setTurnRate(3);
        setVelocityRate(-1 * getVelocityRate());
    }
   
    public void onHitWall(HitWallEvent e) {
        setVelocityRate(-1 * getVelocityRate());
        setTurnRate(getTurnRate() + 2);
        execute();
    }
    
    public void onHitRobot(HitRobotEvent e) {
        double giroDoCanhao = normalRelativeAngleDegrees(e.getBearing() + getHeading() - getGunHeading());
        turnGunRight(giroDoCanhao);
        setFire(3);
        setVelocityRate(getVelocity() + 3);
        execute();
    }
    
    public void onWin(WinEvent e) {
        int uhuu = 1;
        while (true) {
            turnRight(180 * uhuu);
            uhuu = uhuu * -1;
        }
    }
}