// AddTaskForm.tsx

import React from "react";
import "./AddTaskForm.scss";

interface AddTaskFormProps {
  onAddTask: (title: string, description: string) => void;
}

const AddTaskForm: React.FC<AddTaskFormProps> = ({ onAddTask }) => {
  const [newTask, setNewTask] = React.useState("");
  const [newDescription, setNewDescription] = React.useState("");

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (newTask.trim()) {
      onAddTask(newTask, newDescription);
      setNewTask("");
      setNewDescription("");
    }
  };

  return (
    <form className="add-task-form" onSubmit={handleSubmit}>
      <div className="form-row">
        <div className="input-group">
          <div className="add-task-input">
            <input
              type="text"
              placeholder="Add a task"
              value={newTask}
              onChange={(e) => setNewTask(e.target.value)}
            />
          </div>
          <div className="add-description-input">
            <input
              type="text"
              placeholder="Add a description (optional)"
              value={newDescription}
              onChange={(e) => setNewDescription(e.target.value)}
            />
          </div>
        </div>
        <button type="submit" className="add-btn">Add Task</button>
      </div>
    </form>
  );
};

export default AddTaskForm;
