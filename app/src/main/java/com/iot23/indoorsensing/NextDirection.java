package com.iot23.indoorsensing;

import java.util.HashMap;
import java.util.Map;

public class NextDirection {
    public NextDirection() {

    }

    String[][] groupAlpha4f = {
            {"artechne4f", "3"},
            {"426", "6"},
            {"427", "10"},
            {"428", "13"},
            {"429", "16"},
            {"430", "20"},
            {"431", "23"},
            {"432", "26"},
            {"433", "30"},
            {"E", "32"},
            {"434", "33"},
            {"435", "36"},
            {"stairsAlpha_1", "40"},
            {"401", "45"},
            {"402", "50"},
            {"403", "60"},
            {"404", "70"},
            {"405", "80"},
            {"F", "80"},
            {"406", "85"},
            {"G", "90"}
    };
    String[][] groupBeta4f = {
            {"artechne4f", "0"},
            {"425", "2"},
            {"424", "4"},
            {"423", "8"},
            {"422", "12"},
            {"421", "16"},
            {"420", "20"},
            {"419", "24"},
            {"418", "28"},
            {"B", "32"},
            {"417", "36"},
            {"416", "36"},
            {"stairsBeta_1", "40"},
            {"415", "44"},
            {"414", "50"},
            {"413", "60"},
            {"C", "70"},
            {"412", "80"},
            {"411", "90"},
            {"D", "90"}
    };
    String[][] groupCharlie4f = {
            {"E", "0"},
            {"433", "0"},
            {"434", "0"},
            {"betweenEB", "10"},
            {"418", "20"},
            {"B", "20"}
    };
    String[][] groupDelta4f = {
            {"F", "0"},
            {"405", "0"},
            {"betweenFC1", "6"},
            {"betweenFC2", "14"},
            {"betweenFC3", "22"},
            {"412", "30"},
            {"C", "30"}
    };
    String[][] groupEcho4f = {
            {"G", "0"},
            {"407", "0"},
            {"408", "7"},
            {"409", "14"},
            {"410", "21"},
            {"411", "27"},
            {"D", "27"}
    };
    String[][] groupAlpha5f = {
            {"artechne5f", "0"},
            {"526", "5"},
            {"527", "10"},
            {"528", "15"},
            {"529", "20"},
            {"530", "26"},
            {"531", "32"},
            {"E", "32"},
            {"532", "36"},
            {"stairsAlpha", "40"},
            {"501", "44"},
            {"502", "50"},
            {"I", "53"},
            {"503", "58"},
            {"504", "68"},
            {"505", "80"},
            {"F", "80"},
            {"506", "85"},
            {"G", "90"}
    };
    String[][] groupBeta5f = {
            {"artechne5f", "0"},
            {"525", "5"},
            {"524", "11"},
            {"523", "17"},
            {"522", "21"},
            {"521", "25"},
            {"B", "29"},
            {"520", "33"},
            {"519", "36"},
            {"stairsBeta", "40"},
            {"518", "45"},
            {"517", "51"},
            {"516", "55"},
            {"515", "60"},
            {"H", "61"},
            {"514", "65"},
            {"513", "68"},
            {"C", "75"},
            {"512", "80"},
            {"511", "88"},
            {"D", "88"}
    };
    String[][] groupCharlie5f = {
            {"E", "0"},
            {"531", "0"},
            {"between5EB", "10"},
            {"B", "20"}
    };
    String[][] groupDelta5f = {
            {"F", "0"},
            {"505", "0"},
            {"between5FC1", "6"},
            {"between5FC2", "14"},
            {"between5FC3", "22"},
            {"512", "30"},
            {"C", "30"}
    };
    String[][] groupEcho5f = {
            {"G", "0"},
            {"507", "0"},
            {"508", "7"},
            {"509", "14"},
            {"510", "21"},
            {"511", "27"},
            {"D", "27"},
    };
    String[][] groupFox5f = {
            {"I", "0"},
            {"betweenIH", "10"},
            {"H", "20"},
            {"507", "20"}
    };

    public int getDirection (String startPos, String endPos){
        // 아이디어는
        // 1. 같은 라인 그룹에 있으면 각 배열별 정적 각도를 계산
        // 2. 다른 라인 그룹에 있으면 (사실 근접해있으면 그럴일이 없음) 각 라인의 소속 그룹을 찾고, 두 라인의 공통점 기준으로
        // 3. 층 이동이 있으면 방위각 대신 계단을 이용하라는 신호
        // 리턴값 1001 : 에러 // 리턴값 1002 : 계단 사용
        HashMap<String, Integer> groupDegree = new HashMap<>();
        groupDegree.put("4A", 65);
        groupDegree.put("4B", 90);
        groupDegree.put("4C", 180);
        groupDegree.put("4D", 180);
        groupDegree.put("4E", 180);
        groupDegree.put("5A", 65);
        groupDegree.put("5B", 90);
        groupDegree.put("5C", 180);
        groupDegree.put("5D", 180);
        groupDegree.put("5E", 180);
        groupDegree.put("5F", 150);

        // 그룹 안에는 각 그룹의 이름과 위치값이 있음
        HashMap<String, Integer> startGroup = new HashMap<>();
        HashMap<String, Integer> endGroup = new HashMap<>();
        for (String[] selectGroup : groupAlpha4f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("4A", Integer.valueOf(selectGroup[1]) );
            if (selectGroup[0].equals(endPos) ) endGroup.put("4A", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupBeta4f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("4B", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("4B", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupCharlie4f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("4C", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("4C", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupDelta4f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("4D", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("4D", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupEcho4f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("4E", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("4E", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupAlpha5f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("5A", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("5A", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupBeta5f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("5B", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("5B", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupCharlie5f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("5C", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("5C", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupDelta5f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("5D", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("5D", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupEcho5f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("5E", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("5E", Integer.valueOf(selectGroup[1]));
        }
        for (String[] selectGroup : groupFox5f) {
            if (selectGroup[0].equals(startPos) ) startGroup.put("5F", Integer.valueOf(selectGroup[1]));
            if (selectGroup[0].equals(endPos) ) endGroup.put("5F", Integer.valueOf(selectGroup[1]));
        }

        if (startGroup.size() == 0 || endGroup.size() == 0) {
            return 1001;
        }
        // 소속 그룹을 확인해서 같은 그룹이 있으면 같은 그룹 내에서 비교 후 리턴
        Map.Entry<String, Integer> ss1 = null, ss2 = null;
        for (Map.Entry<String, Integer> s1 : startGroup.entrySet()) {
            ss1 = s1;
            for (Map.Entry<String, Integer> s2 : endGroup.entrySet()) {
                ss2 = s2;
                if (s1.getKey() == s2.getKey()) {
                    if (s1.getValue() < s2.getValue()) return groupDegree.get(s1.getKey());
                    else return groupDegree.get(s1.getKey()) + 180;
                }
            }
        }

        // 같은 키가 없을 경우 (솔직히 이 경우는 키가 하나밖에 없는 경우거나 다른 층이거나 둘 중 하나...)
        // 먼저 다른 층인지 확인 후 다른 층이라면 계단을 이용하라고 출력
        // 같은 층이면 두 그룹의 교점을 찾고, 교점 -> 도착지 각도를 출력
        if (ss1.getKey().charAt(0) != ss2.getKey().charAt(0) ) {
            return 1002;
        }

        // 4층일 때의 교점
        if (ss1.getKey().equals("4A")) {
            if (ss2.getKey().equals("4B")) return 90;
            else return 180;
        }
        if (ss1.getKey().equals("4B")) {
            if (ss2.getKey().equals("4A")) return 65;
            else return 0;
        }
        if (ss1.getKey().equals("4C")) {
            if (ss2.getKey().equals("4A")) {
                if (ss2.getValue() < 32) return 65+180;
                else return 65;
            }
            else {
                if (ss2.getValue() < 32) return 270;
                else return 90;
            }
        }
        if (ss1.getKey().equals("4D")) {
            if (ss2.getKey().equals("4A")) {
                if (ss2.getValue() < 80) return 65+180;
                else return 65;
            }
            else {
                if (ss2.getValue() < 70) return 270;
                else return 90;
            }
        }
        if (ss1.getKey().equals("4E")) {
            if (ss2.getKey().equals("4A")) return 65+180;
            else return 270;
        }

        // 5층일 때의 교점
        if (ss1.getKey().equals("5A")) {
            if (ss2.getKey().equals("5B")) return 90;
            else if (ss2.getKey().equals("5F")) return 150;
            else return 180;
        }
        if (ss1.getKey().equals("5B")) {
            if (ss2.getKey().equals("5A")) return 65;
            else if (ss2.getKey().equals("5F")) return 150+180;
            else return 0;
        }
        if (ss1.getKey().equals("5C")) {
            if (ss2.getKey().equals("5A")) {
                if (ss2.getValue() < 32) return 65+180;
                else return 65;
            }
            else {
                if (ss2.getValue() < 29) return 270;
                else return 90;
            }
        }
        if (ss1.getKey().equals("5D")) {
            if (ss2.getKey().equals("5A")) {
                if (ss2.getValue() < 80) return 65+180;
                else return 65;
            }
            else {
                if (ss2.getValue() < 75) return 270;
                else return 90;
            }
        }
        if (ss1.getKey().equals("5E")) {
            if (ss2.getKey().equals("5A")) return 65+180;
            else return 270;
        }
        if (ss1.getKey().equals("5F")) {
            if (ss2.getKey().equals("5A")) {
                if (ss2.getValue() < 53) return 65+180;
                else return 65;
            }
            else {
                if (ss2.getValue() < 61) return 270;
                else return 90;
            }
        }

        return 1001;
    }
}

