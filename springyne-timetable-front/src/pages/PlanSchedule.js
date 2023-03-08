import { useParams } from "react-router-dom";
import { useState, useEffect } from "react";


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
      <h2 className="my-5">
        Planuoti {schedule?.group?.name} grupės tvarkaraštį
      </h2>

      <div className="col-md-8 mb-2">
        {schedule?.group?.program?.subjects?.map((sub) => (
          <button
            type="submit"
            className="btn btn-light me-2 mb-2"
            value={sub.id}
            key={sub.id}
            id={sub.id}
          >
            {sub.subject.name} {sub.hours} 
          </button>
        ))}
      </div>
    </div>
  );
}

export default PlanSchedulePage;
