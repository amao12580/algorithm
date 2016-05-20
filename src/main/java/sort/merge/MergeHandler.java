package sort.merge;

import basic.Comparator;
import basic.Util;
import basic.sort.SortHandlerBehavior;
import basic.sort.Sortable;
import sort.insertion.InsertionHandler;

/**
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2016/5/5
 * Time:11:10
 */
public class MergeHandler extends SortHandlerBehavior {

    private static final int DEFAULT_SPLIT_HALVE_SIZE =2;

    private static final int DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN=(int)Util.pow(DEFAULT_SPLIT_HALVE_SIZE,6);

    /**
     *
     * merge sort(�鲢����)
     *
     * ˼·��
     *  Divide array into two halves.
     *  Recursively sort each half.
     *  Merge two halves.
     *
     *
     *  Abstract in-place merge(ԭ�ع鲢�ĳ��󷽷�)
     *  Given two sorted subarrays a[lo] to a[mid] and a[mid+1] to a[hi],replace with sorted subarray a[lo] to a[hi]
     *
     *
     *  ���裺
     *   �Ƚ�����Ԫ�ظ��Ƶ�aux[]�У��ٹ鲢��a[]�С�
     *   �鲢ʱ���ĸ��жϣ�
     *   �����þ�(ȡ�Ұ��Ԫ��)
     *   �Ұ���þ�(ȡ����Ԫ��)
     *   �Ұ�ߵĵ�ǰԪ��С�����ߵĵ�ǰԪ��(ȡ�Ұ�ߵ�Ԫ��)
     *   �Ұ�ߵĵ�ǰԪ�ش���/�������ߵĵ�ǰԪ��(ȡ���ߵ�Ԫ��)
     *
     *
     * @param originArray ���������
     * @return  ���ź��������
     * @throws Exception
     */
    @Override
    public Comparable[] sort(Comparable[] originArray) throws Exception {
        init(originArray);
        Sortable sortable=new InsertionHandler();
        if(originArray.length<DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN){
            return new InsertionHandler().sort(originArray);
        }
        //1.���ȷݲ��Ϊ����ȳ�����
        Comparable[][] splitedArray=split(originArray);
        //2.ÿ�ݽ�������
        for (int i = 0; i < splitedArray.length; i++) {
            splitedArray[i]=sortable.sort(splitedArray[i]);
        }
        //3.�ϲ�������
        int index=0;
        for (int i = 0; i < splitedArray.length; i+=DEFAULT_SPLIT_HALVE_SIZE) {
            Comparable[] mergredArray;
            if(i+1<splitedArray.length){
                mergredArray=merge(splitedArray[i],splitedArray[i+1]);
            }else{
                mergredArray=splitedArray[i];
            }
            addAll(originArray,index,mergredArray);
            index+=mergredArray.length;
        }
        return originArray;
    }

    private void addAll(Comparable[] originArray, int index, Comparable[] pieceArray) {
        if(originArray==null||pieceArray==null){
            return;
        }
        int j=0;
        for (int i = index; i < (index + pieceArray.length); i++) {
            originArray[i]=pieceArray[j];
            j++;
        }
    }


    public Comparable[][] split(Comparable[] originArray) throws Exception {
        int originArrayLength=originArray.length;
        int pieceNum=originArrayLength/DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN;
        if(pieceNum*DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN<originArrayLength){
            pieceNum++;
        }

        Comparable[][] result=new Comparable[pieceNum][];
        for (int i = 0; i < originArrayLength; i++) {
            int slot=i/DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN;
            int position=i%DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN;
            if(result[slot]==null){
                if((slot+1)<pieceNum){
                    result[slot]=new Comparable[DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN];
                }else{
                    result[slot]=new Comparable[originArrayLength-((pieceNum-1)*DEFAULT_SPLIT_PIECE_ARRAY_LENGTH_MIN)];
                }
            }
            //System.out.println("index:"+index+",slot:"+slot+",position:"+position+",result[slot].length:"+result[slot].length);
            result[slot][position]=originArray[i];
        }
        return result;
    }

    /**
     *
     * http://blog.jobbole.com/100349/
     *
     * �������Ѿ��ź��������ϲ���һ���������顣
     *
     * �ź�����ָ��ֵС��Ԫ�������±�ֵ��С
     *
     * ʱ�临�Ӷ�Ϊ On nΪl���������Ԫ���ܸ���
     *
     * @param leftArray  ��һ���ź��������
     * @param rightArray  �ڶ����ź��������
     * @return �ϲ���ɵ���������
     */
    private Comparable[] merge(Comparable[] leftArray,Comparable[] rightArray){
        int leftLen=leftArray.length;
        int rightLen=rightArray.length;
        int total=leftLen+rightLen;
        Comparable[] result= new Comparable[total];
        int fi=0,si=0,ri=0;
        while (ri<total){
            //System.out.println("ri"+ri+",si:"+si+",fi:"+fi);
            if(fi>leftLen-1){
                result[ri]=rightArray[si];
                si++;
                ri++;
                continue;
            }
            if(si>rightLen-1){
                result[ri]=leftArray[fi];
                fi++;
                ri++;
                continue;
            }
            Comparable f=leftArray[fi];
            Comparable s=rightArray[si];
            if(Comparator.isLTE(f, s)){
                result[ri]=f;
                fi++;
            }else{
                result[ri]=s;
                si++;
            }
            ri++;
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        //���� merge
        Sortable sortable = new MergeHandler();
        //int f[]={-1,0,2,3,7,8,9,10,12,15};
        //int s[]={0,0,1,4,5,6,7,8,11,18};
        //System.out.println(Arrays.toString(new MergeHandler<Integer>().merge(f, s)));




        //���� split
        //Integer[] originArray= Util.getRandomIntegerNumberArray(960);
        //System.out.println("originArray.length:" + originArray.length);
        //Comparable[][] splitedArray=new MergeHandler().split(originArray);
        //for (int i = 0; i < splitedArray.length; i++) {
        //    System.out.println("i:"+i+",len:"+splitedArray[i].length+",data:" + arrayToString(splitedArray[i]));
        //}
        //System.out.println("splitedArray.length:" + splitedArray.length);



        //��������

        benchmark(sortable);



        //Sortable sortable = new MergeHandler();
        //Integer f[] = {5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152,5, 0, 8, 58, 10, 2, 3, 7, 8, 9, 10, 12, 15, -5, 4, 152};
        //Integer[] originArray=Util.getRandomIntegerNumberArray(1000000);
        //List<Integer> list=new ArrayList<>();
        //for (int i = 0; i < originArray.length; i++) {
         //   list.add(originArray[i]);
        //}
        //System.out.println("---:" + Arrays.toString(originArray));
        //System.out.println("originArray.length:" + originArray.length);


        //System.out.println("---:" + Arrays.toString(sortedArray));

        //for (int i = 0; i < originArray.length; i++) {
          //  if(!list.contains(originArray[i])){
            //    System.out.println("error,index:"+i+",data:" + originArray[i]);
           // }
        //}
    }

    private static String arrayToString(Comparable[] comparables) {
        StringBuffer sb=new StringBuffer("[");
        for (int i = 0; i < comparables.length; i++)
            if (comparables[i] != null) {
                sb.append(comparables[i].toString());
                if ((i+1)<comparables.length && i != (comparables.length - 1) && comparables[i+1] != null) {
                    sb.append(",");
                }
            }
        sb.append("]");
        return sb.toString();
    }
}
