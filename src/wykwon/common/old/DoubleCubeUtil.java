package wykwon.common.old;

import org.apache.log4j.Logger;

public class DoubleCubeUtil {
    /**
     * depth�࿡ ��� average�ع�����.
     *
     * @return
     */
    private static Logger logger = Logger.getLogger(DoubleCubeUtil.class);

    public static DoubleMatrix depthAverage(OldDoubleCube cube) {
        double array3D[][][] = cube.referWholeArray();
        int depthSize = cube.getDepthSize();
        int rowSize = cube.getRowSize();
        int colSize = cube.getColSize();
        double array2D[][] = new double[rowSize][colSize];
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                double values[] = new double[depthSize];
                for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
                    values[depthIdx] = array3D[depthIdx][rowIdx][colIdx];
                }
                array2D[rowIdx][colIdx] = average(values);
            }
        }
        return new DoubleMatrix(array2D);
    }

    /**
     * depth���� ��� �ִ� �����͸� ��â row������ �Űܹ�����. �� ���ο� rowSize�� rowSize x depthSize ��ŭ
     * ������ �����°���
     *
     * @param cube
     * @return
     */
    public static DoubleMatrix stretchDepth2Row(OldDoubleCube cube) {
        int depthSize = cube.getDepthSize();
        int rowSize = cube.getRowSize();
        int colSize = cube.getColSize();

        int newRowSize = rowSize * depthSize;
        int newColSize = colSize;

        int newColIdx;
        int newRowIdx;
        DoubleMatrix matrix = new DoubleMatrix(newRowSize, newColSize);
        for (int colIdx = 0; colIdx < colSize; colIdx++) {
            newColIdx = colIdx;
            for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
                for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
                    double value = cube.getValue(depthIdx, rowIdx, colIdx);
                    newRowIdx = depthIdx * rowSize + rowIdx;
                    matrix.setData(newRowIdx, newColIdx, value);
                }
            }
        }
        return matrix;
    }

    /**
     * depth�������� �ڿ��ִ� column vector���� ������ ��ġ��ų��.
     *
     * @param cube
     * @return
     */
    public static DoubleMatrix back2Front_column(OldDoubleCube cube) {
        int depthSize = cube.getDepthSize();
        int rowSize = cube.getRowSize();
        int colSize = cube.getColSize();

        int newColSize = colSize * depthSize;
        int newRowSize = rowSize;
        int newRowIdx;
        int newColIdx;
        DoubleMatrix matrix = new DoubleMatrix(newRowSize, newColSize);
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
                    newColIdx = depthIdx * colSize + colIdx;
                    newRowIdx = rowIdx;
                    double value = cube.getValue(depthIdx, rowIdx, colIdx);
                    matrix.setData(newRowIdx, newColIdx, value);
                }
            }
        }
        return matrix;
    }

    /**
     * row�� depth�� �ٲ������. transpose�� ����
     *
     * @param cube
     * @return
     */
    public static OldDoubleCube transposeDepthRow(OldDoubleCube cube) {

        int depthSize = cube.getDepthSize();
        int rowSize = cube.getRowSize();
        int colSize = cube.getColSize();
        int newDepthSize = rowSize;
        int newRowSize = depthSize;
        int newColSize = colSize;

        int newColIdx;
        int newRowIdx;
        int newDepthIdx;
        OldDoubleCube newCube = new OldDoubleCube(newDepthSize, newRowSize,
                newColSize);
        for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
            for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
                for (int colIdx = 0; colIdx < colSize; colIdx++) {
                    double value = cube.getValue(depthIdx, rowIdx, colIdx);
                    newDepthIdx = rowIdx;
                    newRowIdx = depthIdx;
                    newColIdx = colIdx;
                    newCube.setValue(newDepthIdx, newRowIdx, newColIdx, value);
                }
            }
        }
        return newCube;
    }

    private static double average(double array[]) {
        double sum = 0.0;
        for (int i = 0; i < array.length; i++) {
            sum += array[i];
        }
        return sum / array.length;
    }

    /**
     * Row�� scale�� ���δ�.
     * <p/>
     * row 2���� 1�� �Ǵ� 3���� �Ѱ� �̷������� ����� ��� ���ο� cube�� ����������.
     *
     * @param cube
     * @param divideFactor
     */
    public static OldDoubleCube reduceRowScale(OldDoubleCube cube, int interval) {

        int depthSize = cube.getDepthSize();
        int rowSize = cube.getRowSize();
        int colSize = cube.getColSize();
        int newDepthSize = depthSize;
        int newRowSize = rowSize
                / (interval + 1)
                + (int) Math.ceil((double) (rowSize % (interval + 1))
                / (interval + 1));
        int newColSize = colSize;

        OldDoubleCube newCube = new OldDoubleCube(newDepthSize, newRowSize,
                newColSize);
        for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                for (int rowIdx = 0, newRowIdx = 0; rowIdx < rowSize; rowIdx = rowIdx
                        + interval + 1, newRowIdx++) {
                    double value = cube.getValue(depthIdx, rowIdx, colIdx);
                    newCube.setValue(depthIdx, newRowIdx, colIdx, value);
                }
            }
        }
        return newCube;
    }

    /**
     * cube�� matrix�� array�� ��ȯ�Ѵ�.
     * depth ���� �߸� matrix �� ��ȯ�Ѵ�.
     *
     * @param cube
     * @param depthIdx
     * @return
     */
    public static DoubleMatrix getDepthSlicedMatrix(OldDoubleCube cube,
                                                    int depthIdx) {

        int depthSize = cube.getDepthSize();
        if (depthIdx >= depthSize || depthIdx < 0) {
            throw new RuntimeException("depthIdx is not correct " + depthIdx);
        }
        int rowSize = cube.getRowSize();
        int colSize = cube.getColSize();
        double array2D[][] = new double[rowSize][colSize];
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                array2D[rowIdx][colIdx] = cube.getValue(depthIdx, rowIdx, colIdx);
            }
        }
        return new DoubleMatrix(array2D);
    }

    public static DoubleMatrix[] depthSlice(OldDoubleCube cube) {
        int depthSize = cube.getDepthSize();
        DoubleMatrix matrixArray[] = new DoubleMatrix[depthSize];
        for (int depthIdx = 0; depthIdx < depthSize; depthIdx++) {
            matrixArray[depthIdx] = DoubleCubeUtil.getDepthSlicedMatrix(cube,
                    depthIdx);
        }
        return matrixArray;
    }

    public static void main(String args[]) {

    }

}
