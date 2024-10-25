package p12.exercise;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q> {

    public static final String QUEUE_NOT_AVAILABLE_ERROR = "The queue is not available";

    private final Map<Q, LinkedList<T>> queuesMap;

    public MultiQueueImpl() {
        queuesMap = new HashMap<>();
    }

    /**
     * Search for the queue in the map
     * 
     * @param queue        is the queue that needs ti be checked
     * @param errorMessage is the errore message that needs to be thrown if the
     *                     condition is failed
     * @param contains     weather you want to know if the queue is already in the
     *                     map or not
     * 
     * @throws IllegalArgumentExeption if the condition required is failed
     */
    private void queueSearch(Q queue, String errorMessage, boolean contains) {
        if (queuesMap.keySet().contains(queue) == contains) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    @Override
    public Set<Q> availableQueues() {
        return queuesMap.keySet();
    }

    @Override
    public void openNewQueue(Q queue) {
        queueSearch(queue, "The queue is already Available", true);
        queuesMap.put(queue, new LinkedList<>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        queueSearch(queue, QUEUE_NOT_AVAILABLE_ERROR, false);
        return queuesMap.get(queue).isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) {
        queueSearch(queue, QUEUE_NOT_AVAILABLE_ERROR, false);
        queuesMap.get(queue).addLast(elem);
    }

    @Override
    public T dequeue(Q queue) {
        queueSearch(queue, QUEUE_NOT_AVAILABLE_ERROR, false);
        return queuesMap.get(queue).poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        Map<Q, T> dequeuedItems = new HashMap<>();
        Iterator<Q> iterator = queuesMap.keySet().iterator();
        while (iterator.hasNext()) {
            Q currentQueue = iterator.next();
            dequeuedItems.put(currentQueue, dequeue(currentQueue));
        }
        return dequeuedItems;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> allElements = new HashSet<>();
        for (Q queue : queuesMap.keySet()) {
            allElements.addAll(new HashSet<>(queuesMap.get(queue)));
        }
        return allElements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        queueSearch(queue, QUEUE_NOT_AVAILABLE_ERROR, false);
        List<T> itemsFromSingleQueue = new LinkedList<>();
        int elementCount = queuesMap.get(queue).size();
        for (int i = 0; i < elementCount; i++) {
            itemsFromSingleQueue.add(dequeue(queue));
        }
        return itemsFromSingleQueue;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        queueSearch(queue, QUEUE_NOT_AVAILABLE_ERROR, false);
        if (queuesMap.size() < 2) {
            throw new IllegalStateException("There is no alternative queue for moving elements to");
        }
        List<T> vacantElements = dequeueAllFromQueue(queue);
        queuesMap.remove(queue);
        queuesMap.get(queuesMap.entrySet().iterator().next().getKey()).addAll(vacantElements);
    }

}