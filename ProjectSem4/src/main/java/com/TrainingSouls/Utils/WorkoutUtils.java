package com.TrainingSouls.Utils;

import com.TrainingSouls.Entity.UserProfile;

public class WorkoutUtils {

    public static int calculateSets(UserProfile userProfile) {
        int sets = 2;

        if ("trung cấp".equalsIgnoreCase(userProfile.getLevel())) {
            sets = 3;
        } else if ("cao cấp".equalsIgnoreCase(userProfile.getLevel())) {
            sets = 4;
        }

        if (userProfile.getMuscleMassPercentage() != null &&
                userProfile.getMuscleMassPercentage() > 40 &&
                userProfile.getBodyFatPercentage() != null &&
                userProfile.getBodyFatPercentage() < 20) {
            sets += 1;
        }

        return Math.min(sets, 5);
    }

    public static int calculateReps(UserProfile userProfile) {
        int reps = 10;

        if ("tăng cơ".equalsIgnoreCase(userProfile.getFitnessGoal())) {
            reps = 12;
        } else if ("giảm cân".equalsIgnoreCase(userProfile.getFitnessGoal())) {
            reps = 15;
        }

        if (userProfile.getBodyFatPercentage() != null && userProfile.getBodyFatPercentage() > 30) {
            reps -= 2;
        }

        if (userProfile.getMuscleMassPercentage() != null && userProfile.getMuscleMassPercentage() > 40) {
            reps += 2;
        }

        if ("cao cấp".equalsIgnoreCase(userProfile.getLevel())) {
            reps += 2;
        }

        return Math.max(6, Math.min(reps, 20));
    }

    public static double getRunningDistance(UserProfile userProfile) {
        double baseDistance = 2.0;

        if ("trung cấp".equalsIgnoreCase(userProfile.getLevel())) {
            baseDistance = 3.0;
        } else if ("cao cấp".equalsIgnoreCase(userProfile.getLevel())) {
            baseDistance = 4.0;
        }

        if ("cao".equalsIgnoreCase(userProfile.getActivityLevel())) {
            baseDistance += 1.0;
        }

        if ("giảm mỡ".equalsIgnoreCase(userProfile.getFitnessGoal())) {
            baseDistance += 0.5;
        }

        return Math.min(baseDistance, 6.0);
    }

    public static int calculateRunningTime(UserProfile userProfile, double distance) {
        double pace = 10.0;

        if ("trung cấp".equalsIgnoreCase(userProfile.getLevel())) {
            pace = 8.5;
        } else if ("cao cấp".equalsIgnoreCase(userProfile.getLevel())) {
            pace = 7.0;
        }

        if (userProfile.getMuscleMassPercentage() != null && userProfile.getMuscleMassPercentage() > 40) {
            pace -= 0.5;
        }

        if (userProfile.getBodyFatPercentage() != null && userProfile.getBodyFatPercentage() > 30) {
            pace += 1.0;
        }

        return (int) Math.round(distance * pace);
    }
}

