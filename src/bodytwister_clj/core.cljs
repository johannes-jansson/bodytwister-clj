(ns bodytwister-clj.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

; (println "This text is printed from src/bodytwister-clj/core.cljs. Go ahead and edit it and see reloading in action.")

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
                      :pair []}))

(defn get-current-pair []
  (if (= (count (get @app-state :pair)) 0)
    (str "")
    (str (get (get @app-state :pair) 0)
         " mot "
         (get (get @app-state :pair) 1))))

(defn get-current-score []
  (if (= (get @app-state :points) -1)
    (str "Poäng: -")
    (str "Poäng: " (get @app-state :points))))

(defn get-highscore []
  (if (= (get @app-state :highscore) 0)
    (str "Highscore: -")
    (str "Highscore: " (get @app-state :highscore))))

(defn generate-pair []
  "Generates a new random pair from parts list"
  [(rand-nth parts) (rand-nth parts)])

(defn replace-current-pair []
  (swap! app-state assoc :pair (generate-pair))
  (swap! app-state assoc :points (+ (get @app-state :points) 1))
  ; (swap! (app-state :points) inc)
  )

(defn end-game []
  (println "gameover")
  (swap! app-state assoc :pair [])
  (swap! app-state assoc :highscore (get @app-state :points))
  (swap! app-state assoc :points 0)
  )

(defn component []
  [:div
   [:div {:id "highscore"} (get-highscore)]
   [:div {:id "score"} (get-current-score)]
   [:div {:id "pair"} (get-current-pair)]
   [:div {:id "buttons"}
    [:input {:type "button"
             :value "NEXT"
             :on-click #(replace-current-pair)}]
    [:input {:type "button"
             :value "FAIL"
             :on-click #(end-game)}]]])

(reagent/render-component [component]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
  )
