import { Link } from "react-router-dom";

function ScheduleListPage() {
  return (
    <div className="mx-3">
      <h2 className="my-5">Tvarkaraščiai</h2>

      <div className="d-flex justify-content-end">
        <div className="me-auto d-flex">
          <button className="btn btn-primary mb-5 me-2">
            <Link to="/schedules/create" className="nav-link">
              Pridėti naują tvarkaraštį
            </Link>
          </button>
        </div>
      </div>
    </div>
  );
}

export default ScheduleListPage;
