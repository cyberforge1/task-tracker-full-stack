// HeaderControls.tsx

import React from "react";
import "./HeaderControls.scss";
import logo from "/task-tracker-logo.png";

interface HeaderControlsProps {
  filter: string;
  onFilterChange: (filter: string) => void;
  searchQuery: string;
  onSearch: (searchQuery: string) => void;
  viewMode: string;
  onViewModeChange: (viewMode: string) => void;
}

const HeaderControls: React.FC<HeaderControlsProps> = ({ filter, onFilterChange, searchQuery, onSearch, viewMode, onViewModeChange }) => {
  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    onSearch(e.target.value);
  };

  return (
    <div className="header-controls">
      <div className="title-container">
        <img src={logo} alt="Logo" className="logo" />
        <h1>Task Tracker</h1>
      </div>
      <div className="controls-container">
        <button className="view-mode-btn" onClick={() => onViewModeChange(viewMode === "card" ? "tile" : "card")}>
          {viewMode === "card" ? "Card View" : "List View"}
        </button>
        <select value={filter} onChange={(e) => onFilterChange(e.target.value)}>
          <option value="all">All</option>
          <option value="incomplete">Pending</option>
          <option value="completed">Completed</option>
        </select>
        <div className="search-input">
          <input
            type="text"
            placeholder="Search tasks"
            value={searchQuery}
            onChange={handleSearch}
          />
        </div>
      </div>
    </div>
  );
};

export default HeaderControls;
