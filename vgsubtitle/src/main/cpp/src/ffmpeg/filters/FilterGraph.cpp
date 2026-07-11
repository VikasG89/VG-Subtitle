#include "FilterGraph.h"

namespace vg::subtitle::ffmpeg {

FilterGraph::FilterGraph() = default;
FilterGraph::~FilterGraph() = default;
bool FilterGraph::setup(const std::string& filterDesc) { return true; }

} // namespace vg::subtitle::ffmpeg
