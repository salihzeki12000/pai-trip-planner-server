package wykwon.common;

import org.apache.log4j.Logger;
import org.math.array.DoubleArray;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 4. 3
 * Time: 오후 4:41
 * To change this template use File | Settings | File Templates.
 */
public class DoubleCube {
    private static Logger logger = Logger.getLogger(DoubleCube.class);

    /**
     * double cube
     * <p/>
     * observation * time * dimension
     *
     * @param cube
     * @return
     */
    public static String toString(double cube[][][]) {
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < cube.length; i++) {
            strBuf.append(i + "th matrix\n");
            strBuf.append(DoubleArray.toString("%3.3f", cube[i]) + "\n");
        }
        return strBuf.toString();
    }


    public static double[][][][] timeSlicingFixedInterval(double cube[][][], int timeWidth) {
        int depthSize = cube.length;
        int timeSize = cube[0].length;
        logger.debug("timeSize=" + timeSize);
        int sliceSize = timeSize / timeWidth;
        logger.debug("sliceSize=" + sliceSize);
        for (int i = 0; i < sliceSize; i++) {
            int startIdx = timeWidth * i;        // inclusive  idx
            int endIdx = startIdx + timeWidth - 1;                  //inclusive idx


            logger.debug(startIdx + ":" + endIdx);
        }
        //for (int d=0; d<depthSize; d++){


        return null;
    }

    /**
     * row indice에 따른 subcube를 만들어 반환
     *
     * @param cube
     * @param rowIndices sort가 되어 있어야 함
     * @return
     */
    public static double[][][] subCubeByRow(double cube[][][], int... rowIndices) {
        int depthSize = cube.length;
        int rowSize = cube[0].length;

        double subCube[][][] = new double[depthSize][rowIndices.length][];

        int newRowIdx = 0;
        for (int r = 0; r < rowSize; r++) {
            int ret = Arrays.binarySearch(rowIndices, r);
            logger.debug(ret);
            if (ret >= 0) {
                for (int d = 0; d < depthSize; d++) {
                    subCube[d][newRowIdx] = cube[d][r].clone();
                }
                newRowIdx++;
            }
        }
        return subCube;
    }

    public static double[][][] asCube(List<double[][]> matrixArray) {
        double newCube[][][] = new double[matrixArray.size()][][];
        for (int i = 0; i < matrixArray.size(); i++) {
            newCube[i] = matrixArray.get(i);
        }
        return newCube;
    }

    /**
     * row indice에 따른 subcube를 만들어 반환
     *
     * @param cube
     * @param colIndices sort가 되어 있어야 함
     * @return
     */
    public static double[][][] subCubeByCol(double cube[][][], int... colIndices) {
        int depthSize = cube.length;
        int rowSize = cube[0].length;


        double subCube[][][] = new double[depthSize][rowSize][colIndices.length];

        int newColIdx = 0;
        for (int c = 0; c < rowSize; c++) {
            int ret = Arrays.binarySearch(colIndices, c);
            if (ret >= 0) {
                for (int d = 0; d < depthSize; d++) {
                    for (int r = 0; r < rowSize; r++) {
                        logger.debug(ret);
                        subCube[d][r][newColIdx] = cube[d][r][c];
                    }
                }
                newColIdx++;
            }

        }
        return subCube;
    }

    /**
     * cube의 row와 col을 transpose함 .
     *
     * @param cube cube 구조 [depth][row][col] 순서임
     * @return
     */
    public static double[][][] transposeRowCol(double cube[][][]) {
        double newCube[][][] = new double[cube.length][][];
        for (int i = 0; i < cube.length; i++) {
            newCube[i] = DoubleArray.transpose(cube[i]).clone();
        }
        return newCube;
    }


    public static double[][][] transposeDepthRow(double cube[][][]) {
        throw new RuntimeException("Not yet Implemented");
    }

    public static double[][][] transposeDepthCol(double cube[][][]) {
        throw new RuntimeException("Not yet Implemented");
    }

    public static void main(String[] args) {

        double cube[][][] = {
                {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}, {7.0, 8.0}},

                {{9, 10}, {11.0, 12.0}, {13.0, 14.0}, {15.0, 16.0}}
        };
        logger.debug(DoubleCube.toString(cube));
        logger.debug(DoubleCube.timeSlicingFixedInterval(cube, 3));
        double subcube[][][] = DoubleCube.subCubeByRow(cube, 1);
        logger.debug(DoubleCube.toString(subcube));
        logger.debug(DoubleCube.toString(DoubleCube.subCubeByCol(cube, 0)));

//        logger.debug(DoubleArray.toString(rbind(cube)));
    }

}
