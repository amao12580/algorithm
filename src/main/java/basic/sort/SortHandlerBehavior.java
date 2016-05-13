package basic.sort;

import basic.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/13
 * Time:14:45
 */
public abstract class SortHandlerBehavior<E extends Comparable> implements Sortable<E>{

    public void swap(E[] array,int firstIndex,int secondIndex){
        swapIfNotEqual(array,firstIndex,array[firstIndex],secondIndex,array[secondIndex]);
    }

    public void swap(E[] array,int firstIndex,E firstValue,int secondIndex){
        swapIfNotEqual(array,firstIndex,firstValue,secondIndex,array[secondIndex]);
    }

    public void swap(E[] array,int firstIndex,int secondIndex,E secondValue){
        swapIfNotEqual(array,firstIndex,array[firstIndex],secondIndex,secondValue);
    }

    public void swap(E[] array,int firstIndex,E firstValue,int secondIndex,E secondValue){
        swapIfNotEqual(array,firstIndex,firstValue,secondIndex,secondValue);
    }

    public void swapIfNotEqual(E[] array,int firstIndex,E firstValue,int secondIndex,E secondValue){
        if(Comparator.isEQUAL(firstValue, secondValue)){
            swapAlways(array, firstIndex, firstValue, secondIndex, secondValue);
        }
    }

    public void swapIfLessThan(E[] array,int firstIndex,E firstValue,int secondIndex,E secondValue){
        if(Comparator.isLT(firstValue, secondValue)){
            swapAlways(array, firstIndex, firstValue, secondIndex, secondValue);
        }
    }

    public void swapIfLessThan(E[] array, int firstIndex, E firstValue, int secondIndex) {
        swapIfLessThan(array,firstIndex,firstValue,secondIndex,array[secondIndex]);
    }

    private void swapAlways(E[] array,int firstIndex,E firstValue,int secondIndex,E secondValue){
        array[firstIndex]=secondValue;
        array[secondIndex]=firstValue;
    }

    public void swapIfLessThan(E[] array,int firstIndex,int secondIndex){
        swapIfLessThan(array, firstIndex, array[firstIndex], secondIndex, array[secondIndex]);
    }

    public void init(E[] originArray) throws IllegalAccessException {
        if(originArray==null){
            throw new IllegalAccessException("不可以接受null参数");
        }
    }

    public void init(E[]... originArray) throws IllegalAccessException {
        for (int i = 0; i < originArray.length; i++) {
            init(originArray[i]);
        }
    }
}
