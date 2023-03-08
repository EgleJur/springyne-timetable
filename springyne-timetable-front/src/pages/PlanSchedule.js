import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import Calendar from "../components/Calendar";

function PlanSchedulePage() {
   const params = useParams();
   const [schedule, setSchedule] = useState({});

   useEffect(() => {
      fetch("/api/v1/schedules/" + params.id)
         .then((response) => response.json())
         .then((jsonResponse) => setSchedule(jsonResponse));
   }, []);

   return (
    <div className="mx-3">
      <h2 className="my-5">Planuoti {schedule?.group?.name} grupės tvarkaraštį</h2> 
      
<Calendar />
    </div >
  );
}

export default PlanSchedulePage;
