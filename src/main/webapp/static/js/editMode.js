function toggleEdit() {
  const isEditMode =
    document.getElementById("editMode").style.display === "block";

  if (isEditMode) {
    document.getElementById("editMode").style.display = "none";
    document.getElementById("viewMode").style.display = "block";
    updateUrl(false);
  } else {
    document.getElementById("editMode").style.display = "block";
    document.getElementById("viewMode").style.display = "none";
    updateUrl(true);
  }
}

function updateUrl(isEdit) {
  const currentUrl = new URL(window.location.href);
  if (isEdit) {
    currentUrl.searchParams.set("edit", "");
  } else {
    currentUrl.searchParams.delete("edit");
  }
  window.history.replaceState({}, "", currentUrl);
}

function cancelEdit() {
  toggleEdit();
}
