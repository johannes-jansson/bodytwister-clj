(ns bodytwister-clj.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)
;(println "end-game")

(def parts (list "hand"
                 "hand"
                 "armbåge"
                 "armbåge"
                 "arm"
                 "arm"
                 "axel"
                 "axel"
                 "hals"
                 "haka"
                 "huvud"
                 "panna"
                 "rygg"
                 "mage"
                 "knä"
                 "knä"
                 "smalben"
                 "smalben"
                 "fot"
                 "fot"
                 "häl"
                 "häl"))

; app-state
(def app-state (atom {:text "Bodytwister"
                      :points -1
                      :highscore 0
                      :gameover 0
                      :started 0
                      :color "blue"
                      :instructions 0
                      :pair (list)}))


; This method handles all state modification
(defn assoc-atom [key, function]
  "Associate atom key with new value defined by function"
  (swap! app-state assoc key function))

; This method handles all state getting
(defn deref-atom [key]
  "Dereferences the atom value for key"
  (get @app-state key))
 

(defn generate-pair []
  "Generates a new random pair from parts list"
  (take 2 (shuffle parts)))

(defn replace-current-pair []
  "Replaces the current pair and updates points"
  (assoc-atom :pair (generate-pair))
  (assoc-atom :points (+ (deref-atom :points) 1))
  )

; app transitions
(defn start-game []
  (replace-current-pair)
  (assoc-atom :instructions 0)
  (assoc-atom :color "green")
  (assoc-atom :started 1))

(defn show-instructions []
  (assoc-atom :instructions 1))

(defn end-game []
  (assoc-atom :pair [])
  (assoc-atom :color "red")
  (if (> (deref-atom :points) (deref-atom :highscore))
    (assoc-atom :highscore (deref-atom :points)))
  (assoc-atom :gameover 1))

(defn start-over []
  (assoc-atom :points -1)
  (assoc-atom :color "green")
  (replace-current-pair)
  (assoc-atom :gameover 0))

; Methods for getting app states and rendering web elements
(defn get-current-score []
  [:div {:id "score"}
   (if (not= (deref-atom :points) -1)
         [:p (deref-atom :points)])])

(defn get-highscore []
  [:div {:id "highscore"}
   (if (not= (deref-atom :highscore) 0)
     [:p 
      [:img {:src "assets/star.svg"}]
      (deref-atom :highscore)])])

(defn get-startscreen []
  (if (and (= (deref-atom :started) 0) (= (deref-atom :instructions) 0))
    [:div {:id "startscreen"}
     [:p "BODYTWISTER"]]
    [:div {:id "startscreen"}]))

(defn get-instructions []
  (if (= (deref-atom :instructions) 1)
    [:div {:id "instructions"}
     [:h1 "Såhär spelar du"]
     [:p "Lorem ipsum dolor sit amet"]]
    [:div {:id "instructions"}]))

(defn get-gameover []
  (if (= (deref-atom :gameover) 1)
    [:div {:id "gameover"}
     [:h1 "GAME OVER"]
     [:p (str "Result: " (deref-atom :points))]]
    [:div {:id "gameover"}]))

(defn get-current-pair []
  (if (= (count (deref-atom :pair)) 0)
    [:div {:id "current-pair"}]
    [:div {:id "current-pair"}
     [:div {:id "first"} (nth (deref-atom :pair) 0)]
     [:div {:id "middle"} " mot "]
     [:div {:id "last"} (nth (deref-atom :pair) 1)]]))

(defn get-startscreen-buttons []
  (println "startscreen-buttons")
  [:div {:id "buttons"}
   [:input {:type "button"
            :value "INSTRUCTIONS"
            :on-click #(show-instructions)}]
   [:input {:type "button"
            :value "START"
            :on-click #(start-game)}]])

(defn get-instructions-buttons []
  (println "instructions-buttons")
  [:div {:id "buttons"}
   [:input {:type "button"
            :value "START"
            :on-click #(start-game)}]])

(defn get-ongoing-buttons []
  (println "ongoing-buttons")
  [:div {:id "buttons"}
   [:input {:type "button"
            :value "NEXT"
            :on-click #(replace-current-pair)}]
   [:input {:type "button"
            :value "FAIL"
            :on-click #(end-game)}]])

(defn get-gameover-buttons []
  (println "gameover-buttons")
  [:div {:id "buttons"}
   [:input {:type "button"
            :value "AGAIN"
            :on-click #(start-over)}]])

(defn get-buttons []
  (if (= (deref-atom :instructions) 1)
    (get-instructions-buttons)
    (if (= (deref-atom :started) 0) 
      (get-startscreen-buttons)
      (if (= (deref-atom :gameover) 1)
        (get-gameover-buttons)
        (get-ongoing-buttons)))))

; Main app component
(defn component []
  [:div {:id "component" :class (deref-atom :color)}
   (get-current-score)
   (get-highscore)
   (get-startscreen)
   (get-instructions)
   (get-gameover)
   (get-current-pair)
   (get-buttons)])

(reagent/render-component [component]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
