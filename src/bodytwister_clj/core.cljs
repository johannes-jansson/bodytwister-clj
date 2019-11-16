(ns bodytwister-clj.core
  (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

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
                      :pair []}))

; functions for updating the pair
(defn generate-pair []
  "Generates a new random pair from parts list"
  [(rand-nth parts) (rand-nth parts)])

(defn replace-current-pair []
  (swap! app-state assoc :pair (generate-pair))
  (swap! app-state assoc :points (+ (get @app-state :points) 1))
  ; (swap! (app-state :points) inc)
  )

; app transitions
(defn start-game []
  (replace-current-pair)
  (swap! app-state assoc :instructions 0)
  (swap! app-state assoc :color "green")
  (swap! app-state assoc :started 1)
  )

(defn show-instructions []
  (swap! app-state assoc :instructions 1)
  )

(defn end-game []
  (println "gameover")
  (swap! app-state assoc :pair [])
  (swap! app-state assoc :color "red")
  (if (> (get @app-state :points) (get @app-state :highscore))
    (swap! app-state assoc :highscore (get @app-state :points)))
  (swap! app-state assoc :gameover 1))

(defn start-over []
  (swap! app-state assoc :points -1)
  (swap! app-state assoc :color "green")
  (replace-current-pair)
  (swap! app-state assoc :gameover 0))

; getters/renderers
(defn get-current-score []
  [:div {:id "score"}
   [:p (if (= (get @app-state :points) -1)
         (str "-")
         (str (get @app-state :points)))]])

(defn get-highscore []
  [:div {:id "highscore"}
   [:p (str "Highscore: "
            (if (= (get @app-state :highscore) 0)
              (str "-")
              (str(get @app-state :highscore))))]])

(defn get-startscreen []
  (if (and (= (get @app-state :started) 0) (= (get @app-state :instructions) 0))
    [:div {:id "startscreen"}
     [:p "BODYTWISTER"]
     [:input {:type "button"
              :value "INSTRUCTIONS"
              :on-click #(show-instructions)}]
     [:input {:type "button"
              :value "START"
              :on-click #(start-game)}]]
    [:div {:id "startscreen"}]))

(defn get-instructions []
  (if (= (get @app-state :instructions) 1)
    [:div {:id "instructions"}
     [:h1 "Såhär spelar du"]
     [:p "Lorem ipsum dolor sit amet"]
     [:input {:type "button"
              :value "START"
              :on-click #(start-game)}]]
    [:div {:id "instructions"}]))

(defn get-gameover []
  [:div {:id "gameover"}
   (if (= (get @app-state :gameover) 1)
     [:div
      [:h1 "GAME OVER"]
      [:p (str "Result: " (get @app-state :points))]
      [:input {:type "button"
               :value "AGAIN"
               :on-click #(start-over)}]])])

(defn get-current-pair []
  (if (= (count (get @app-state :pair)) 0)
    [:div {:id "current-pair"}]
    [:div {:id "current-pair"}
     [:div {:id "first"} (get (get @app-state :pair) 0)]
     [:div {:id "middle"} " mot "]
     [:div {:id "last"} (get (get @app-state :pair) 1)]]))

(defn get-buttons []
  (if (and (= (get @app-state :gameover) 0)(= (get @app-state :started) 1))
    [:div {:id "buttons"}
     [:input {:type "button"
              :value "NEXT"
              :on-click #(replace-current-pair)}]
     [:input {:type "button"
              :value "FAIL"
              :on-click #(end-game)}]]
    [:div {:id "buttons"}]))

(defn component []
  [:div {:id "component" :class (get @app-state :color)}
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
