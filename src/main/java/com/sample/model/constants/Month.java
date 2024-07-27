package com.sample.model.constants;

public enum Month { // 스크래핑한 month(달) 값이 문자열(ex: JAN) 이기에 숫자로 바꾸는 작업

    JAN("Jan", 1),
    FEB("Feb", 2),
    MAR("Mar", 3),
    APR("Apr", 4),
    MAY("May", 5),
    JUN("Jun", 6),
    JUL("Jul", 7),
    AUG("Aug", 8),
    SEP("Sep", 9),
    OCT("Oct", 10),
    NOV("Nov", 11),
    DEC("Dec", 12);

    private String s;
    private int number;

    // 생성자
    Month(String s, int n) {
        this.s = s;
        this.number = n;
    }

    public static int strToNumber(String s) {
        for (var m : Month.values()) { // value를 순회
            if (m.s.equals(s)) {
                return m.number;
            }
        }
        // 값을 못찾으면
        return -1;
    }

}
