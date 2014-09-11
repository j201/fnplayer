(ns fnplayer.core
  (:require [fnplayer.synth :as synth]
            [fnplayer.canvas :as canvas]))

;; time, state -> [scale-pos, new-state]
(def note-equation (fn [t _]
                     [(+ 0.5 (* 0.25 (+ (.sin js/Math (* t 5))
                                        (.sin js/Math (* t 4)))))
                      nil]))
(def pentatonic [0 3 5 7 10 12])

;; 0 <= x <= 1, scale is a vector of integers > 0
(defn note-in-scale [x scale]
  (nth scale (min (dec (count scale))
                  (int (* x (count scale))))))

(defn looper []
  (let [start-time (atom nil)]
    (fn [cb]
      (js/requestAnimationFrame (fn [t]
                                  (when (nil? @start-time)
                                    (reset! start-time t))
                                  (cb (- t @start-time)))))))

(defn synth-loop [scale equation]
  (let [my-looper (looper)]
    ((fn inner [eq-state poss note t]
       (let [[scale-pos new-state] (equation (/ t 1000) eq-state)
             new-note (note-in-scale scale-pos scale)]
         (when (not= new-note note)
           (synth/osc new-note (/ t 1000) 0.5))
         (canvas/draw-positions poss)
         (my-looper #(inner new-state (take 5 (cons scale-pos poss)) note %))))
     nil '(0 0 0 0 0) 0 0)))

(synth-loop pentatonic note-equation)
