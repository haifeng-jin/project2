package expression;

import storageManager.Tuple;

interface Satisfiable {
    boolean satisfy(Tuple tuple);
}
