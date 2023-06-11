package com.iot23.indoorsensing;

// 여기의 inputData 부분에 거리정보를 입력
public class ClassroomGraphData {

    ClassroomGraph data;

    public ClassroomGraphData() {
        data = new ClassroomGraph();
    }

    public ClassroomGraph inputData() {
        // 전체 노드 위치 입력
        {
            this.data.addVertex("426");
            this.data.addVertex("427");
            this.data.addVertex("428");
            this.data.addVertex("429");
            this.data.addVertex("430");
            this.data.addVertex("431");
            this.data.addVertex("432");
            this.data.addVertex("435");
            this.data.addVertex("stairsAlpha_1");
            this.data.addVertex("401");
            this.data.addVertex("402");
            this.data.addVertex("403");
            this.data.addVertex("404");
            this.data.addVertex("406");
            this.data.addVertex("425");
            this.data.addVertex("424");
            this.data.addVertex("423");
            this.data.addVertex("422");
            this.data.addVertex("421");
            this.data.addVertex("420");
            this.data.addVertex("419");
            this.data.addVertex("417");
            this.data.addVertex("416");
            this.data.addVertex("stairsBeta_1");
            this.data.addVertex("415");
            this.data.addVertex("414");
            this.data.addVertex("433");
            this.data.addVertex("434");
            this.data.addVertex("betweenEB");
            this.data.addVertex("418");
            this.data.addVertex("405");
            this.data.addVertex("betweenFC1");
            this.data.addVertex("betweenFC2");
            this.data.addVertex("betweenFC3");
            this.data.addVertex("412");
            this.data.addVertex("413");
            this.data.addVertex("407");
            this.data.addVertex("408");
            this.data.addVertex("409");
            this.data.addVertex("410");
            this.data.addVertex("411");
            this.data.addVertex("artechne4f");
        }
        {
            this.data.addVertex("501");
            this.data.addVertex("502");
            this.data.addVertex("503");
            this.data.addVertex("504");
            this.data.addVertex("505");
            this.data.addVertex("506");
            this.data.addVertex("507");
            this.data.addVertex("508");
            this.data.addVertex("509");
            this.data.addVertex("510");
            this.data.addVertex("511");
            this.data.addVertex("512");
            this.data.addVertex("513");
            this.data.addVertex("514");
            this.data.addVertex("515");
            this.data.addVertex("516");
            this.data.addVertex("517");
            this.data.addVertex("518");
            this.data.addVertex("519");
            this.data.addVertex("520");
            this.data.addVertex("521");
            this.data.addVertex("522");
            this.data.addVertex("523");
            this.data.addVertex("524");
            this.data.addVertex("525");
            this.data.addVertex("526");
            this.data.addVertex("527");
            this.data.addVertex("528");
            this.data.addVertex("529");
            this.data.addVertex("530");
            this.data.addVertex("531");
            this.data.addVertex("532");
            this.data.addVertex("stairsAlpha");
            this.data.addVertex("stairsBeta");
            this.data.addVertex("between5EB");
            this.data.addVertex("between5FC1");
            this.data.addVertex("between5FC2");
            this.data.addVertex("between5FC3");
            this.data.addVertex("betweenIH");
            this.data.addVertex("artechne5f");
        }
        // ...노가다...

        // 각 노드의 연결점
        {
            this.data.addEdge("artechne4f", "425", 3);
            this.data.addEdge("artechne4f", "426", 3);
            this.data.addEdge("426", "427", 4);
            this.data.addEdge("427", "428", 3);
            this.data.addEdge("428", "429", 3);
            this.data.addEdge("429", "430", 4);
            this.data.addEdge("430", "431", 3);
            this.data.addEdge("431", "432", 3);
            this.data.addEdge("432", "433", 4);
            this.data.addEdge("433", "434", 3);
            this.data.addEdge("434", "435", 3);
            this.data.addEdge("435", "stairsAlpha_1", 4);
            this.data.addEdge("stairsAlpha_1", "401", 5);
            this.data.addEdge("401", "402", 5);
            this.data.addEdge("402", "403", 10);
            this.data.addEdge("403", "404", 10);
            this.data.addEdge("404", "405", 10);
            this.data.addEdge("405", "406", 5);
            this.data.addEdge("406", "407", 5);
            this.data.addEdge("425", "424", 4);
            this.data.addEdge("424", "423", 4);
            this.data.addEdge("423", "422", 4);
            this.data.addEdge("422", "421", 4);
            this.data.addEdge("421", "420", 4);
            this.data.addEdge("420", "419", 4);
            this.data.addEdge("419", "418", 4);
            this.data.addEdge("418", "417", 8);
            this.data.addEdge("417", "416", 2);
            this.data.addEdge("416", "stairsBeta_1", 2);
            this.data.addEdge("stairsBeta_1", "415", 4);
            this.data.addEdge("415", "414", 6);
            this.data.addEdge("414", "413", 10);
            this.data.addEdge("413", "412", 10);
            this.data.addEdge("412", "411", 10);
            this.data.addEdge("434", "betweenEB", 10);
            this.data.addEdge("betweenEB", "418", 10);
            this.data.addEdge("405", "betweenFC1", 6);
            this.data.addEdge("betweenFC1", "betweenFC2", 8);
            this.data.addEdge("betweenFC2", "betweenFC3", 8);
            this.data.addEdge("betweenFC3", "412", 8);
            this.data.addEdge("407", "408", 7);
            this.data.addEdge("408", "409", 7);
            this.data.addEdge("409", "410", 7);
            this.data.addEdge("410", "411", 6);
        }
        {
            this.data.addEdge("artechne5f", "525", 3);
            this.data.addEdge("artechne5f", "526", 3);
            this.data.addEdge("526", "527", 5);
            this.data.addEdge("527", "528", 5);
            this.data.addEdge("528", "529", 5);
            this.data.addEdge("529", "530", 6);
            this.data.addEdge("530", "531", 6);
            this.data.addEdge("531", "532", 4); // E
            this.data.addEdge("532", "stairsAlpha", 4);
            this.data.addEdge("stairsAlpha", "501", 4);
            this.data.addEdge("501", "502", 6);
            this.data.addEdge("502", "503", 8);
            this.data.addEdge("503", "504", 10);
            this.data.addEdge("504", "505", 12);
            this.data.addEdge("505", "506", 5);
            this.data.addEdge("506", "507", 5);
            this.data.addEdge("525", "524", 6);
            this.data.addEdge("524", "523", 6);
            this.data.addEdge("523", "522", 4);
            this.data.addEdge("522", "521", 4);
            this.data.addEdge("521", "520", 7);
            this.data.addEdge("520", "519", 3);
            this.data.addEdge("519", "stairsBeta", 4);
            this.data.addEdge("stairsBeta", "518", 5);
            this.data.addEdge("518", "517", 6);
            this.data.addEdge("517", "516", 4);
            this.data.addEdge("516", "515", 5);
            this.data.addEdge("515", "514", 5);
            this.data.addEdge("514", "513", 3);
            this.data.addEdge("513", "512", 12);
            this.data.addEdge("512", "511", 8);
            this.data.addEdge("531", "between5EB", 10);
            this.data.addEdge("between5EB", "520", 10);
            this.data.addEdge("505", "between5FC1", 6);
            this.data.addEdge("between5FC1", "between5FC2", 8);
            this.data.addEdge("between5FC2", "between5FC3", 8);
            this.data.addEdge("between5FC3", "512", 8);
            this.data.addEdge("507", "508", 7);
            this.data.addEdge("508", "509", 7);
            this.data.addEdge("509", "510", 7);
            this.data.addEdge("510", "511", 6);
            this.data.addEdge("502", "betweenIH", 10);
            this.data.addEdge("503", "betweenIH", 10);
            this.data.addEdge("betweenIH", "515", 10);
        }
        {
            this.data.addEdge("stairsAlpha", "stairsAlpha_1", 7);
            this.data.addEdge("stairsBeta", "stairsBeta_1", 7);
            this.data.addEdge("408", "508", 7);
            this.data.addEdge("artechne4f", "artechne5f", 7);

        }

        return this.data;
    }
}
