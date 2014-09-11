(ns fnplayer.canvas)

(def canvas (.querySelector js/document "canvas"))
(def ctx (.getContext canvas "2d"))
(def canvas-width 500)
(def canvas-height 500)

(defn red-dot-colour [intensity]
  (str "rgb(" (min 255 (int (* intensity 255))) ",0,0)"))

(defn draw-positions [poss]
  (.clearRect ctx 0 0 canvas-width canvas-height)
  (doseq [[pos index] (map list poss (range))]
    (set! (.-fillStyle ctx) (red-dot-colour (- 1 (/ index (count poss)))))
    (.beginPath ctx)
    (.arc ctx
          (- (/ canvas-width 2) (* 20 index))
          (* canvas-height (- 1 pos))
          10 0 (* 2 (.-PI js/Math)) false)
    (.closePath ctx)
    (.fill ctx)))
