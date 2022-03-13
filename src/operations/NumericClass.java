package operations;

public class NumericClass implements Numeric{
    @Override
    public int searchElement(int index, String element, String[] array) {

        int current = searchElementInArray(index, element, array);
        if(current == -1){
            current = searchElementInArrayItem(index, element, array);
        }

        return current;

    }

    private static int searchElementInArray(int indexStart, String element, String[] arr){
        int index = -1;

        for(int i = indexStart; i < arr.length; i++){

            if(arr[i].equals(element)){
                index = i;
            }
        }

        return index;
    }
    //2.1 Если элемент массива содержит слово тогда возращает индекс, если нет -1
    private static int searchElementInArrayItem(int indexStart, String element, String[] arr){
        int index = -1;

        for(int i = indexStart; i < arr.length; i++){

            if(arr[i].contains(element)){
                index = i;
                break;
            }
        }

        return index;
    }
}
